package com.moocrest.webhook.builder;

import com.moocrest.webhook.model.WebhookEmbed;
import com.moocrest.webhook.model.WebhookMessage;

import java.util.ArrayList;
import java.util.List;

public class WebhookBuilder {
    private String content;
    private String username;
    private String avatarUrl;
    private List<WebhookEmbed> embeds;
    private String threadName;
    private Integer flags;

    public WebhookBuilder() {
        this.embeds = new ArrayList<>();
    }

    public WebhookBuilder content(String content) {
        this.content = content;
        return this;
    }

    public WebhookBuilder username(String username) {
        this.username = username;
        return this;
    }

    public WebhookBuilder avatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public WebhookBuilder addEmbed(WebhookEmbed embed) {
        if (embed != null) {
            this.embeds.add(embed);
        }
        return this;
    }

    public WebhookBuilder embeds(List<WebhookEmbed> embeds) {
        this.embeds = embeds != null ? new ArrayList<>(embeds) : new ArrayList<>();
        return this;
    }

    public WebhookBuilder threadName(String threadName) {
        this.threadName = threadName;
        return this;
    }

    public WebhookBuilder flags(Integer flags) {
        this.flags = flags;
        return this;
    }

    public WebhookMessage build() {
        List<WebhookEmbed> embedList = embeds.isEmpty() ? null : new ArrayList<>(embeds);
        return new WebhookMessage(content, username, avatarUrl, embedList, threadName, flags);
    }
}