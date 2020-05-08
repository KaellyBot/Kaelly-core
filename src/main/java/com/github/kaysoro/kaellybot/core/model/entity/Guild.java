package com.github.kaysoro.kaellybot.core.model.entity;

import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.model.constant.Server;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "guilds")
public class Guild {

    @Id
    private String id;

    private Language language;

    private Server server;

    private String prefix;
}
