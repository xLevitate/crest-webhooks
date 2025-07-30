package com.moocrest.webhook;

import com.moocrest.webhook.model.WebhookMessage;
import com.moocrest.webhook.sender.WebhookSender;
import com.moocrest.webhook.util.WebhookException;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class WebhookSenderTest {

    @Test
    void testInvalidWebhookUrlThrowsException() {
        WebhookMessage message = WebhookMessage.builder()
                .content("Test message")
                .build();

        CompletableFuture<Boolean> future = WebhookSender.sendWebhook("invalid-url", message);

        assertThrows(Exception.class, future::join);
    }

    @Test
    void testSendSimpleMessage() {
        CompletableFuture<Boolean> future = WebhookSender.sendSimpleMessage(
                "https://discord.com/api/webhooks/123456789/test",
                "Simple test message");

        assertNotNull(future);
        assertThrows(Exception.class, future::join);
    }

    @Test
    void testSendSimpleEmbed() {
        CompletableFuture<Boolean> future = WebhookSender.sendSimpleEmbed(
                "https://discord.com/api/webhooks/123456789/test",
                "Test Title",
                "Test Description");

        assertNotNull(future);
        assertThrows(Exception.class, future::join);
    }

    @Test
    void testSendWebhookWithTimeout() {
        WebhookMessage message = WebhookMessage.builder()
                .content("Test message with timeout")
                .build();

        assertDoesNotThrow(() -> {
            CompletableFuture<Boolean> future = WebhookSender.sendWebhook(
                    "https://discord.com/api/webhooks/123456789/test",
                    message,
                    Duration.ofSeconds(5));

            assertNotNull(future);
        });
    }

    @Test
    void testNullMessageHandling() {
        assertThrows(Exception.class, () -> {
            WebhookSender.sendWebhook("https://discord.com/api/webhooks/123456789/test", null)
                    .join();
        });
    }
}