package com.moocrest.webhook.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

public final class WebhookUrlValidator {

    private static final Pattern DISCORD_WEBHOOK_PATTERN = Pattern.compile(
            "^https://discord(?:app)?\\.com/api/webhooks/\\d+/[\\w-]+(?:\\?[\\w&=%-]*)?$",
            Pattern.CASE_INSENSITIVE);

    private WebhookUrlValidator() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean isValid(String webhookUrl) {
        if (webhookUrl == null || webhookUrl.trim().isEmpty()) {
            return false;
        }

        try {
            new URL(webhookUrl);
            return DISCORD_WEBHOOK_PATTERN.matcher(webhookUrl.trim()).matches();
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static void validate(String webhookUrl) {
        if (!isValid(webhookUrl)) {
            throw WebhookException.invalidUrl(webhookUrl);
        }
    }

    public static String normalize(String webhookUrl) {
        if (webhookUrl == null) {
            return null;
        }

        String normalized = webhookUrl.trim();
        validate(normalized);
        return normalized;
    }
}