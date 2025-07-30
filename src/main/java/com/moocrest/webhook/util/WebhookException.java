package com.moocrest.webhook.util;

public class WebhookException extends RuntimeException {

    public WebhookException(String message) {
        super(message);
    }

    public WebhookException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebhookException(Throwable cause) {
        super(cause);
    }

    public static WebhookException invalidUrl(String url) {
        return new WebhookException("Invalid Discord webhook URL: " + url);
    }

    public static WebhookException httpError(int statusCode, String responseBody) {
        return new WebhookException("HTTP error " + statusCode + ": " + responseBody);
    }

    public static WebhookException timeout(String url) {
        return new WebhookException("Request timeout for webhook URL: " + url);
    }

    public static WebhookException serializationError(Throwable cause) {
        return new WebhookException("Failed to serialize webhook message", cause);
    }

    public static WebhookException networkError(String url, Throwable cause) {
        return new WebhookException("Network error for webhook URL: " + url, cause);
    }
}