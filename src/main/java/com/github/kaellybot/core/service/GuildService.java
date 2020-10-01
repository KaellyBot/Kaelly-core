package com.github.kaellybot.core.service;

import com.github.kaellybot.core.repository.GuildRepository;
import com.github.kaellybot.core.model.constant.Constants;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Guild;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
@AllArgsConstructor
public class GuildService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuildService.class);

    private final GuildRepository guildRepository;

    public Mono<Boolean> existsById(Snowflake id){
        return guildRepository.existsById(id.asString());
    }

    public Mono<com.github.kaellybot.core.model.entity.Guild> save(Guild guild){
        LOGGER.info("Guild[id={}] added", guild.getId());
        return guildRepository.save(com.github.kaellybot.core.model.entity.Guild.builder()
                .id(guild.getId().asString())
                .language(Constants.DEFAULT_LANGUAGE)
                .prefix(Constants.DEFAULT_PREFIX)
                .channelLanguageList(Collections.emptyList())
                .channelServersList(Collections.emptyList())
                .build());
    }

    public Mono<com.github.kaellybot.core.model.entity.Guild> findById(Snowflake id){
        return guildRepository.findById(id.asString());
    }

    public Mono<Void> deleteById(Snowflake id){
        LOGGER.info("Guild[id={}] removed", id);
        return guildRepository.deleteById(id.asString());
    }
}
