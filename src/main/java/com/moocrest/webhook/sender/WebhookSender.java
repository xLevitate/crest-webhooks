package com.moocrest.webhook.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.moocrest.webhook.model.WebhookMessage;
import com.moocrest.webhook.util.WebhookException;
import com.moocrest.webhook.util.WebhookUrlValidator;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

public final class WebhookSender {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final int MAX_RETRIES = 3;
    private static final Duration RETRY_DELAY = Duration.ofMillis(1000);
    private static final AtomicLong lastRequestTime = new AtomicLong(0);
    private static final long RATE_LIMIT_MILLIS = 1000;

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(DEFAULT_TIMEOUT)
            .build();

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private WebhookSender() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static CompletableFuture<Boolean> sendWebhook(String webhookUrl, WebhookMessage message) {
        return sendWebhook(webhookUrl, message, DEFAULT_TIMEOUT);
    }

    public static CompletableFuture<Boolean> sendWebhook(String webhookUrl, WebhookMessage message, Duration timeout) {
        return sendWebhookWithRetry(webhookUrl, message, timeout, 0);
    }

    private static CompletableFuture<Boolean> sendWebhookWithRetry(String webhookUrl, WebhookMessage message,
            Duration timeout, int attempt) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                WebhookUrlValidator.validate(webhookUrl);
                String json = serializeMessage(message);

                enforceRateLimit();

                HttpRequest request = buildRequest(webhookUrl, json, timeout);
                return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenCompose(response -> handleResponse(response, webhookUrl, message, timeout, attempt))
                        .join();

            } catch (Exception e) {
                if (attempt < MAX_RETRIES) {
                    return CompletableFuture.supplyAsync(() -> {
                        try {
                            Thread.sleep(RETRY_DELAY.toMillis() * (attempt + 1));
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                        return sendWebhookWithRetry(webhookUrl, message, timeout, attempt + 1).join();
                    }).join();
                }
                throw new WebhookException("Failed to send webhook after " + MAX_RETRIES + " attempts", e);
            }
        });
    }

    private static CompletableFuture<Boolean> handleResponse(HttpResponse<String> response, String webhookUrl,
            WebhookMessage message, Duration timeout, int attempt) {
        int statusCode = response.statusCode();

        if (statusCode >= 200 && statusCode < 300) {
            return CompletableFuture.completedFuture(true);
        }

        if (statusCode == 429) {
            String retryAfter = response.headers().firstValue("Retry-After").orElse("1");
            long delaySeconds = Long.parseLong(retryAfter);

            if (attempt < MAX_RETRIES) {
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(delaySeconds * 1000);
                        return sendWebhookWithRetry(webhookUrl, message, timeout, attempt + 1).join();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                });
            }
        }

        if (statusCode >= 400 && statusCode < 500) {
            throw WebhookException.httpError(statusCode, response.body());
        }

        if (attempt < MAX_RETRIES) {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(RETRY_DELAY.toMillis() * (attempt + 1));
                    return sendWebhookWithRetry(webhookUrl, message, timeout, attempt + 1).join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            });
        }

        throw WebhookException.httpError(statusCode, response.body());
    }

    private static String serializeMessage(WebhookMessage message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw WebhookException.serializationError(e);
        }
    }

    private static HttpRequest buildRequest(String webhookUrl, String json, Duration timeout) {
        return HttpRequest.newBuilder()
                .uri(URI.create(webhookUrl))
                .timeout(timeout)
                .header("Content-Type", "application/json")
                .header("User-Agent", "Discord-Webhook-Java/1.0")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
    }

    private static void enforceRateLimit() {
        long currentTime = System.currentTimeMillis();
        long lastTime = lastRequestTime.get();
        long timeDiff = currentTime - lastTime;

        if (timeDiff < RATE_LIMIT_MILLIS) {
            long sleepTime = RATE_LIMIT_MILLIS - timeDiff;
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        lastRequestTime.set(System.currentTimeMillis());
    }

    public static CompletableFuture<Boolean> sendSimpleMessage(String webhookUrl, String content) {
        WebhookMessage message = WebhookMessage.builder()
                .content(content)
                .build();
        return sendWebhook(webhookUrl, message);
    }

    public static CompletableFuture<Boolean> sendSimpleEmbed(String webhookUrl, String title, String description) {
        WebhookMessage message = WebhookMessage.builder()
                .addEmbed(com.moocrest.webhook.model.WebhookEmbed.builder()
                        .title(title)
                        .description(description)
                        .build())
                .build();
        return sendWebhook(webhookUrl, message);
    }
}