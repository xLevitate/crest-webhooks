package com.moocrest.webhook.examples;

import com.moocrest.webhook.model.WebhookEmbed;
import com.moocrest.webhook.model.WebhookMessage;
import com.moocrest.webhook.sender.WebhookSender;

import java.time.Duration;

public class WebhookUsageExample {

    private static final String WEBHOOK_URL = "https://discord.com/api/webhooks/YOUR_WEBHOOK_ID/YOUR_WEBHOOK_TOKEN";

    public static void main(String[] args) {
        basicMessageExample();
        advancedEmbedExample();
        minecraftServerStatusExample();
        playerJoinExample();
        errorNotificationExample();
    }

    public static void basicMessageExample() {
        WebhookMessage message = WebhookMessage.builder()
                .content("Hello from the webhook library!")
                .username("My Bot")
                .avatarUrl("https://example.com/bot-avatar.png")
                .build();

        WebhookSender.sendWebhook(WEBHOOK_URL, message)
                .thenAccept(success -> {
                    if (success) {
                        System.out.println("Message sent successfully!");
                    } else {
                        System.out.println("Failed to send message");
                    }
                })
                .exceptionally(throwable -> {
                    System.err.println("Error: " + throwable.getMessage());
                    return null;
                });
    }

    public static void advancedEmbedExample() {
        WebhookMessage message = WebhookMessage.builder()
                .username("Server Monitor")
                .addEmbed(WebhookEmbed.builder()
                        .title("Server Status Report")
                        .description("Current server statistics")
                        .color(0x00FF00)
                        .timestampNow()
                        .author("Minecraft Server", "https://minecraft.net", "https://minecraft.net/favicon.ico")
                        .thumbnail("https://example.com/server-icon.png")
                        .addField("Players Online", "15/50", true)
                        .addField("Uptime", "2 days, 5 hours", true)
                        .addField("TPS", "19.8", true)
                        .addField("Memory Usage", "4.2GB / 8GB", false)
                        .footer("Last updated", "https://example.com/footer-icon.png")
                        .build())
                .build();

        WebhookSender.sendWebhook(WEBHOOK_URL, message, Duration.ofSeconds(15))
                .thenAccept(success -> System.out.println("Advanced embed sent: " + success));
    }

    public static void minecraftServerStatusExample() {
        WebhookMessage startupMessage = WebhookMessage.builder()
                .content("🟢 Server is starting up...")
                .username("Minecraft Server")
                .avatarUrl("https://example.com/minecraft-icon.png")
                .addEmbed(WebhookEmbed.builder()
                        .title("Server Started")
                        .description("The Minecraft server is now online and ready for players!")
                        .color(0x55FF55)
                        .timestampNow()
                        .addField("Version", "1.20.4", true)
                        .addField("Max Players", "100", true)
                        .addField("Difficulty", "Hard", true)
                        .image("https://example.com/server-banner.png")
                        .footer("Server powered by Crest Webhooks")
                        .build())
                .threadName("server-status")
                .build();

        WebhookSender.sendWebhook(WEBHOOK_URL, startupMessage)
                .thenAccept(success -> System.out.println("Server startup notification sent: " + success));
    }

    public static void playerJoinExample() {
        String playerName = "Steve";

        WebhookMessage joinMessage = WebhookMessage.builder()
                .addEmbed(WebhookEmbed.builder()
                        .description("**" + playerName + "** joined the server")
                        .color(0x00AA00)
                        .timestampNow()
                        .thumbnail("https://mc-heads.net/avatar/" + playerName + "/64")
                        .footer("Players online: 16/50")
                        .build())
                .build();

        WebhookSender.sendWebhook(WEBHOOK_URL, joinMessage)
                .thenAccept(success -> System.out.println("Player join notification sent: " + success))
                .exceptionally(throwable -> {
                    System.err.println("Failed to send join notification: " + throwable.getMessage());
                    return null;
                });
    }

    public static void errorNotificationExample() {
        WebhookMessage errorMessage = WebhookMessage.builder()
                .content("⚠️ **Server Alert**")
                .username("Server Monitor")
                .addEmbed(WebhookEmbed.builder()
                        .title("Critical Error Detected")
                        .description("A critical error has occurred on the server")
                        .color(0xFF0000)
                        .timestampNow()
                        .addField("Error Type", "OutOfMemoryError", true)
                        .addField("Affected Plugin", "MyPlugin v1.2.3", true)
                        .addField("Stack Trace",
                                "```java\njava.lang.OutOfMemoryError: Java heap space\n    at com.example.MyPlugin.process(MyPlugin.java:42)\n```",
                                false)
                        .footer("Automatic restart in 60 seconds")
                        .build())
                .build();

        WebhookSender.sendWebhook(WEBHOOK_URL, errorMessage)
                .thenAccept(success -> System.out.println("Error notification sent: " + success));
    }

    public static void simpleApiUsage() {
        WebhookSender.sendSimpleMessage(WEBHOOK_URL, "Simple message using convenience method")
                .thenAccept(success -> System.out.println("Simple message sent: " + success));

        WebhookSender
                .sendSimpleEmbed(WEBHOOK_URL, "Simple Embed", "This is a simple embed using the convenience method")
                .thenAccept(success -> System.out.println("Simple embed sent: " + success));
    }
}