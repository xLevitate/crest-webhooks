# Crest Webhooks

A lightweight, production-ready Discord webhook utility library for Minecraft plugin development.

## Features

- ✅ **Builder Pattern**: Fluent API for webhook creation
- ✅ **Async Support**: CompletableFuture-based asynchronous operations
- ✅ **Thread-Safe**: Safe for concurrent usage
- ✅ **Rate Limiting**: Built-in Discord rate limit handling
- ✅ **Retry Logic**: Automatic retry with exponential backoff
- ✅ **Production Ready**: No comments, self-explanatory code
- ✅ **Java 17**: Modern Java with Records and HttpClient

## Quick Start

### Basic Usage

```java
WebhookMessage message = WebhookMessage.builder()
    .content("Server started!")
    .username("Minecraft Bot")
    .addEmbed(WebhookEmbed.builder()
        .title("Server Status")
        .description("The server is now online")
        .color(0x00FF00)
        .build())
    .build();

WebhookSender.sendWebhook("https://discord.com/api/webhooks/...", message)
    .thenAccept(success -> {
        if (success) {
            System.out.println("Webhook sent successfully!");
        }
    });
```

### Simple API

```java
WebhookSender.sendSimpleMessage(webhookUrl, "Quick message")
    .thenAccept(success -> System.out.println("Sent: " + success));

WebhookSender.sendSimpleEmbed(webhookUrl, "Title", "Description")
    .thenAccept(success -> System.out.println("Sent: " + success));
```

## Architecture

### Package Structure

```
com.moocrest.webhook/
├── model/              # Record classes (WebhookMessage, WebhookEmbed)
├── builder/            # Builder classes for fluent API
├── sender/             # HTTP sending logic (WebhookSender)
└── util/              # Utilities (validation, exceptions)
```

### Core Components

#### WebhookMessage Record
Contains all Discord webhook fields:
- Basic fields: `content`, `username`, `avatarUrl`
- Embeds support with full Discord specification
- Thread support via `threadName`
- Discord flags and additional features

#### WebhookEmbed Record
Full Discord embed specification:
- Basic: `title`, `description`, `url`, `color`, `timestamp`
- Rich content: `author`, `footer`, `image`, `thumbnail`
- Fields with inline support
- Nested records for complex objects

#### WebhookSender Class
Main utility with async operations:
- `sendWebhook(url, message)` - Basic sending
- `sendWebhook(url, message, timeout)` - With custom timeout
- `sendSimpleMessage(url, content)` - Convenience method
- `sendSimpleEmbed(url, title, desc)` - Convenience method

## Advanced Usage

### Complex Embeds

```java
WebhookMessage message = WebhookMessage.builder()
    .username("Server Monitor")
    .addEmbed(WebhookEmbed.builder()
        .title("Server Status Report")
        .description("Current server statistics")
        .color(0x00FF00)
        .timestampNow()
        .author("Minecraft Server", "https://minecraft.net", "icon.png")
        .thumbnail("https://example.com/server-icon.png")
        .addField("Players Online", "15/50", true)
        .addField("Uptime", "2 days, 5 hours", true)
        .addField("TPS", "19.8", true)
        .addField("Memory Usage", "4.2GB / 8GB", false)
        .footer("Last updated", "footer-icon.png")
        .image("https://example.com/server-banner.png")
        .build())
    .threadName("server-status")
    .build();
```

### Error Handling

```java
WebhookSender.sendWebhook(webhookUrl, message)
    .thenAccept(success -> {
        if (success) {
            plugin.getLogger().info("Notification sent");
        } else {
            plugin.getLogger().warning("Notification failed");
        }
    })
    .exceptionally(throwable -> {
        plugin.getLogger().severe("Webhook error: " + throwable.getMessage());
        return null;
    });
```

### Custom Timeouts

```java
WebhookSender.sendWebhook(webhookUrl, message, Duration.ofSeconds(30))
    .thenAccept(success -> handleResult(success));
```

## Built-in Features

### Rate Limiting
- Automatic 1-second rate limiting
- HTTP 429 handling with Retry-After header support
- Prevents Discord API abuse

### Retry Logic
- Up to 3 automatic retries
- Exponential backoff strategy
- Network error recovery

### Validation
- Discord webhook URL validation
- Malformed URL detection
- Comprehensive error messages

### Thread Safety
- All operations are thread-safe
- Concurrent webhook sending supported
- Atomic rate limiting implementation

## Requirements

- Java 17+
- Jackson for JSON serialization
- No additional external dependencies

## Minecraft Plugin Integration

### Bukkit/Spigot Example

```java
public class MyPlugin extends JavaPlugin {
    private static final String WEBHOOK_URL = "your-webhook-url";
    
    @Override
    public void onEnable() {
        WebhookSender.sendSimpleMessage(WEBHOOK_URL, "✅ Server started!")
            .thenAccept(success -> getLogger().info("Start notification: " + success));
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        
        WebhookMessage message = WebhookMessage.builder()
            .addEmbed(WebhookEmbed.builder()
                .description("**" + playerName + "** joined the server")
                .color(0x00AA00)
                .timestampNow()
                .thumbnail("https://mc-heads.net/avatar/" + playerName)
                .build())
            .build();
            
        WebhookSender.sendWebhook(WEBHOOK_URL, message);
    }
}
```

## Performance

- **Memory Efficient**: No unnecessary object retention
- **Fast Serialization**: Optimized Jackson configuration
- **Connection Reuse**: Efficient HTTP client implementation
- **Non-blocking**: All operations are asynchronous

## Exception Handling

The library provides comprehensive exception handling:

- `WebhookException.invalidUrl()` - Invalid webhook URLs
- `WebhookException.httpError()` - HTTP response errors
- `WebhookException.timeout()` - Request timeouts
- `WebhookException.serializationError()` - JSON serialization issues
- `WebhookException.networkError()` - Network connectivity problems

## License

MIT License - See LICENSE file for details.

## Contributing

This library follows a production-ready, comment-free coding style. All code should be self-explanatory through proper naming and structure.