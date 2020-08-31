package com.github.kaellybot.core.model.entity;

import com.github.kaellybot.commons.model.constants.Language;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "almanax-webhook")
public class AlmanaxWebhook {

    @Id
    private String webhookId;

    private String webhookToken;

    private String guildId;

    private Language language;
}
