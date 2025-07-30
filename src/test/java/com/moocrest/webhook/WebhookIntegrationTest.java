package com.moocrest.webhook;

import com.moocrest.webhook.model.WebhookEmbed;
import com.moocrest.webhook.model.WebhookMessage;
import com.moocrest.webhook.util.WebhookUrlValidator;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class WebhookIntegrationTest {

        private static final String VALID_WEBHOOK_URL = "https://discord.com/api/webhooks/123456789/abcdefghijklmnopqrstuvwxyz";

        @Test
        void testWebhookMessageBuilder() {
                WebhookMessage message = WebhookMessage.builder()
                                .content("Test message")
                                .username("Test Bot")
                                .avatarUrl("https://example.com/avatar.png")
                                .addEmbed(WebhookEmbed.builder()
                                                .title("Test Embed")
                                                .description("This is a test embed")
                                                .color(0x00FF00)
                                                .timestampNow()
                                                .addField("Field 1", "Value 1", true)
                                                .addField("Field 2", "Value 2", false)
                                                .footer("Footer text", "https://example.com/footer.png")
                                                .build())
                                .threadName("test-thread")
                                .build();

                assertNotNull(message);
                assertEquals("Test message", message.content());
                assertEquals("Test Bot", message.username());
                assertEquals("https://example.com/avatar.png", message.avatarUrl());
                assertEquals("test-thread", message.threadName());
                assertNotNull(message.embeds());
                assertEquals(1, message.embeds().size());

                WebhookEmbed embed = message.embeds().get(0);
                assertEquals("Test Embed", embed.title());
                assertEquals("This is a test embed", embed.description());
                assertEquals(Integer.valueOf(0x00FF00), embed.color());
                assertNotNull(embed.timestamp());
                assertNotNull(embed.fields());
                assertEquals(2, embed.fields().size());
                assertNotNull(embed.footer());
                assertEquals("Footer text", embed.footer().text());
        }

        @Test
        void testWebhookUrlValidator() {
                assertTrue(
                                WebhookUrlValidator.isValid(
                                                "https://discord.com/api/webhooks/123456789/abcdefghijklmnopqrstuvwxyz"));
                assertTrue(WebhookUrlValidator
                                .isValid("https://discordapp.com/api/webhooks/123456789/abcdefghijklmnopqrstuvwxyz"));

                assertFalse(WebhookUrlValidator.isValid("https://example.com/webhook"));
                assertFalse(WebhookUrlValidator.isValid("invalid-url"));
                assertFalse(WebhookUrlValidator.isValid(""));
                assertFalse(WebhookUrlValidator.isValid(null));

                assertDoesNotThrow(() -> WebhookUrlValidator.validate(VALID_WEBHOOK_URL));
                assertThrows(Exception.class, () -> WebhookUrlValidator.validate("invalid-url"));
        }

        @Test
        void testEmbedBuilder() {
                WebhookEmbed embed = WebhookEmbed.builder()
                                .title("Test Title")
                                .description("Test Description")
                                .url("https://example.com")
                                .color(0xFF0000)
                                .timestamp(Instant.now())
                                .author("Author Name", "https://example.com", "https://example.com/icon.png")
                                .footer("Footer", "https://example.com/footer.png")
                                .image("https://example.com/image.png")
                                .thumbnail("https://example.com/thumb.png")
                                .addField("Inline Field", "Value", true)
                                .addField("Regular Field", "Value", false)
                                .build();

                assertNotNull(embed);
                assertEquals("Test Title", embed.title());
                assertEquals("Test Description", embed.description());
                assertEquals("https://example.com", embed.url());
                assertEquals(Integer.valueOf(0xFF0000), embed.color());
                assertNotNull(embed.timestamp());
                assertNotNull(embed.author());
                assertNotNull(embed.footer());
                assertNotNull(embed.image());
                assertNotNull(embed.thumbnail());
                assertNotNull(embed.fields());
                assertEquals(2, embed.fields().size());
                assertTrue(embed.fields().get(0).inline());
                assertFalse(embed.fields().get(1).inline());
        }

        @Test
        void testToBuilderMethods() {
                WebhookEmbed originalEmbed = WebhookEmbed.builder()
                                .title("Original Title")
                                .description("Original Description")
                                .color(0x00FF00)
                                .build();

                WebhookEmbed modifiedEmbed = originalEmbed.toBuilder()
                                .title("Modified Title")
                                .build();

                assertEquals("Modified Title", modifiedEmbed.title());
                assertEquals("Original Description", modifiedEmbed.description());
                assertEquals(originalEmbed.color(), modifiedEmbed.color());

                WebhookMessage originalMessage = WebhookMessage.builder()
                                .content("Original Content")
                                .username("Original User")
                                .build();

                WebhookMessage modifiedMessage = originalMessage.toBuilder()
                                .content("Modified Content")
                                .build();

                assertEquals("Modified Content", modifiedMessage.content());
                assertEquals("Original User", modifiedMessage.username());
        }
}