package com.moocrest.webhook.builder;

import com.moocrest.webhook.model.WebhookEmbed;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class WebhookEmbedBuilder {
    private String title;
    private String description;
    private String url;
    private Integer color;
    private Instant timestamp;
    private WebhookEmbed.Footer footer;
    private WebhookEmbed.Image image;
    private WebhookEmbed.Thumbnail thumbnail;
    private WebhookEmbed.Author author;
    private List<WebhookEmbed.Field> fields;

    public WebhookEmbedBuilder() {
        this.fields = new ArrayList<>();
    }

    public WebhookEmbedBuilder title(String title) {
        this.title = title;
        return this;
    }

    public WebhookEmbedBuilder description(String description) {
        this.description = description;
        return this;
    }

    public WebhookEmbedBuilder url(String url) {
        this.url = url;
        return this;
    }

    public WebhookEmbedBuilder color(int color) {
        this.color = color;
        return this;
    }

    public WebhookEmbedBuilder color(Integer color) {
        this.color = color;
        return this;
    }

    public WebhookEmbedBuilder timestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public WebhookEmbedBuilder timestampNow() {
        this.timestamp = Instant.now();
        return this;
    }

    public WebhookEmbedBuilder footer(String text) {
        this.footer = new WebhookEmbed.Footer(text, null);
        return this;
    }

    public WebhookEmbedBuilder footer(String text, String iconUrl) {
        this.footer = new WebhookEmbed.Footer(text, iconUrl);
        return this;
    }

    public WebhookEmbedBuilder footer(WebhookEmbed.Footer footer) {
        this.footer = footer;
        return this;
    }

    public WebhookEmbedBuilder image(String url) {
        this.image = new WebhookEmbed.Image(url);
        return this;
    }

    public WebhookEmbedBuilder image(WebhookEmbed.Image image) {
        this.image = image;
        return this;
    }

    public WebhookEmbedBuilder thumbnail(String url) {
        this.thumbnail = new WebhookEmbed.Thumbnail(url);
        return this;
    }

    public WebhookEmbedBuilder thumbnail(WebhookEmbed.Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public WebhookEmbedBuilder author(String name) {
        this.author = new WebhookEmbed.Author(name, null, null);
        return this;
    }

    public WebhookEmbedBuilder author(String name, String url) {
        this.author = new WebhookEmbed.Author(name, url, null);
        return this;
    }

    public WebhookEmbedBuilder author(String name, String url, String iconUrl) {
        this.author = new WebhookEmbed.Author(name, url, iconUrl);
        return this;
    }

    public WebhookEmbedBuilder author(WebhookEmbed.Author author) {
        this.author = author;
        return this;
    }

    public WebhookEmbedBuilder addField(String name, String value) {
        this.fields.add(new WebhookEmbed.Field(name, value, false));
        return this;
    }

    public WebhookEmbedBuilder addField(String name, String value, boolean inline) {
        this.fields.add(new WebhookEmbed.Field(name, value, inline));
        return this;
    }

    public WebhookEmbedBuilder addField(WebhookEmbed.Field field) {
        if (field != null) {
            this.fields.add(field);
        }
        return this;
    }

    public WebhookEmbedBuilder fields(List<WebhookEmbed.Field> fields) {
        this.fields = fields != null ? new ArrayList<>(fields) : new ArrayList<>();
        return this;
    }

    public WebhookEmbed build() {
        List<WebhookEmbed.Field> fieldList = fields.isEmpty() ? null : new ArrayList<>(fields);
        return new WebhookEmbed(title, description, url, color, timestamp, footer, image, thumbnail, author, fieldList);
    }
}