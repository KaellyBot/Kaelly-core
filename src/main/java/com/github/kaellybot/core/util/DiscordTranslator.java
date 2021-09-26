package com.github.kaellybot.core.util;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.model.entity.Server;
import com.github.kaellybot.core.model.entity.Guild;
import com.github.kaellybot.commons.util.Translator;
import com.github.kaellybot.core.service.GuildService;
import discord4j.core.object.command.Interaction;
import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.github.kaellybot.core.model.constant.Constants.*;

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
                .map(guild -> guild.getChannelLanguageList().stream()
                        .filter(channelLanguage -> channelLanguage.getId().equals(message.getChannelId().asString()))
                        .findFirst()
                        .map(Guild.ChannelLanguage::getLanguage)
                        .orElse(guild.getLanguage()))
                .defaultIfEmpty(DEFAULT_LANGUAGE);
    }

    public Mono<Language> getLanguage(Interaction interaction){
        return interaction.getGuild()
                .flatMap(guild -> guildService.findById(guild.getId()))
                .map(guild -> guild.getChannelLanguageList().stream()
                        .filter(channelLanguage -> channelLanguage.getId().equals(interaction.getChannelId().asString()))
                        .findFirst()
                        .map(Guild.ChannelLanguage::getLanguage)
                        .orElse(guild.getLanguage()))
                .defaultIfEmpty(DEFAULT_LANGUAGE);
    }

    public Mono<Server> getServer(Message message){
        return message.getGuild()
                .flatMap(guild -> guildService.findById(guild.getId()))
                .map(guild -> guild.getChannelServersList().stream()
                        .filter(channelServer -> channelServer.getId().equals(message.getChannelId().asString()))
                        .findFirst()
                        .map(Guild.ChannelServer::getServer)
                        .orElse(Optional.ofNullable(guild.getServer()).orElse(UNKNOWN_SERVER)));
    }

    public Mono<Server> getServer(Interaction interaction){
        return interaction.getGuild()
                .flatMap(guild -> guildService.findById(guild.getId()))
                .map(guild -> guild.getChannelServersList().stream()
                        .filter(channelServer -> channelServer.getId().equals(interaction.getChannelId().asString()))
                        .findFirst()
                        .map(Guild.ChannelServer::getServer)
                        .orElse(Optional.ofNullable(guild.getServer()).orElse(UNKNOWN_SERVER)));
    }
}