package com.moocrest.webhook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.exlll.configlib.Configuration;
import com.moocrest.webhook.builder.WebhookBuilder;

import java.util.List;

@Configuration
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record WebhookMessage(
        String content,
        String username,
        @JsonProperty("avatar_url") String avatarUrl,
        List<WebhookEmbed> embeds,
        @JsonProperty("thread_name") String threadName,
        Integer flags) {
    public static WebhookBuilder builder() {
        return new WebhookBuilder();
    }

    public WebhookBuilder toBuilder() {
        return new WebhookBuilder()
                .content(content)
                .username(username)
                .avatarUrl(avatarUrl)
                .embeds(embeds)
                .threadName(threadName)
                .flags(flags);
    }
}