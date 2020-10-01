package com.github.kaellybot.core.util;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.model.entity.Guild;
import com.github.kaellybot.commons.util.Translator;
import com.github.kaellybot.core.service.GuildService;
import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.github.kaellybot.core.model.constant.Constants.DEFAULT_LANGUAGE;
import static com.github.kaellybot.core.model.constant.Constants.DEFAULT_PREFIX;

@Component
public class DiscordTranslator extends Translator {

    private final GuildService guildService;

    public DiscordTranslator(GuildService guildService){
        super();
        this.guildService = guildService;
    }

    public Mono<Language> getLanguage(Message message){
        return message.getGuild()
                .flatMap(guild -> guildService.findById(guild.getId()))
                .map(Guild::getLanguage)
                .defaultIfEmpty(DEFAULT_LANGUAGE);
    }

    public Mono<String> getPrefix(Message message){
        return message.getGuild()
                .flatMap(guild -> guildService.findById(guild.getId()))
                .map(Guild::getPrefix)
                .defaultIfEmpty(DEFAULT_PREFIX);
    }
}
