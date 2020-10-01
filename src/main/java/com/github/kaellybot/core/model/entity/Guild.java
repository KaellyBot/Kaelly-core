package com.github.kaellybot.core.model.entity;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.model.entity.Server;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "guilds")
public class Guild {

    @Id
    private String id;

    private Language language;

    private Server server;

    private String prefix;

    private List<ChannelLanguage> channelLanguageList;

    private List<ChannelServer> channelServersList;

    @Data
    @Builder
    public static class ChannelLanguage {
        @Id
        private String id;
        private Language language;
    }

    @Data
    @Builder
    public static class ChannelServer {
        private String id;
        private Server server;
    }
}
