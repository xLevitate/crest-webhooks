package com.moocrest.webhook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moocrest.webhook.builder.WebhookEmbedBuilder;

import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record WebhookEmbed(
        String title,
        String description,
        String url,
        Integer color,
        Instant timestamp,
        Footer footer,
        Image image,
        Thumbnail thumbnail,
        Author author,
        List<Field> fields) {
    public static WebhookEmbedBuilder builder() {
        return new WebhookEmbedBuilder();
    }

    public WebhookEmbedBuilder toBuilder() {
        return new WebhookEmbedBuilder()
                .title(title)
                .description(description)
                .url(url)
                .color(color)
                .timestamp(timestamp)
                .footer(footer)
                .image(image)
                .thumbnail(thumbnail)
                .author(author)
                .fields(fields);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Footer(
            String text,
            @JsonProperty("icon_url") String iconUrl) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Image(
            String url,
            @JsonProperty("proxy_url") String proxyUrl,
            Integer height,
            Integer width) {
        public Image(String url) {
            this(url, null, null, null);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Thumbnail(
            String url,
            @JsonProperty("proxy_url") String proxyUrl,
            Integer height,
            Integer width) {
        public Thumbnail(String url) {
            this(url, null, null, null);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Author(
            String name,
            String url,
            @JsonProperty("icon_url") String iconUrl) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Field(
            String name,
            String value,
            Boolean inline) {
        public Field(String name, String value) {
            this(name, value, false);
        }
    }
}