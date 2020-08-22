package com.github.kaysoro.kaellybot.core.model.entity;

import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.model.constant.Server;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "almanax-webhook")
public class AlmanaxWebhook {

    @Id
    private String channelId;

    private String guildId;

    private String webhookId;
}
