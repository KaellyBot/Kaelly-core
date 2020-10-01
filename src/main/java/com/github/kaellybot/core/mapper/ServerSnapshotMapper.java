package com.github.kaellybot.core.mapper;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.model.entity.Server;
import com.github.kaellybot.core.model.constant.Constants;
import com.github.kaellybot.core.model.entity.Guild;
import com.github.kaellybot.core.util.DiscordTranslator;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.spec.EmbedCreateSpec;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.kaellybot.core.model.constant.Constants.GAME;
import static com.github.kaellybot.core.model.constant.Constants.UNKNOWN_SERVER;

@Component
@AllArgsConstructor
public class ServerSnapshotMapper {

    private final DiscordTranslator translator;

    public void decorateSpec(EmbedCreateSpec spec, discord4j.core.object.entity.Guild discordGuild,
                             List<GuildChannel> channels, Guild guild, Server currentServer,
                             List<Server> serverList, Language language){
        spec.setTitle(translator.getLabel(language, "server.status_title", GAME.getName().toUpperCase()))
                .setThumbnail(currentServer.getImgUrl())
                .setFooter(Constants.GAME.getName(), Constants.GAME.getIcon());

        if (! currentServer.equals(UNKNOWN_SERVER)){
            spec.setDescription(translator.getLabel(language, "server.known_server", currentServer, GAME.getName()));
            spec.addField(":book:", getUsedServerListing(discordGuild, guild.getServer(), channels,
                    guild.getChannelServersList(), language), true);
        }
        else
            spec.setDescription(translator.getLabel(language, "server.unknown_server", GAME.getName()));

        spec.addField(translator.getLabel(language, "server.available_servers"),
                getAvailableServers(serverList, language), true);
    }

    private String getUsedServerListing(discord4j.core.object.entity.Guild guild, Server guildServer,
                                        List<GuildChannel> channels, List<Guild.ChannelServer>  channelServers,
                                        Language language){
        Map<String, GuildChannel> channelsMap = channels.parallelStream()
                .collect(Collectors.toMap(channel -> channel.getId().asString(), Function.identity()));
        StringBuilder st = new StringBuilder(translator
                .getLabel(language, "server.listing", guild.getName(), guildServer)).append("\n");

        for(Guild.ChannelServer channelServer : channelServers)
            if (channelsMap.containsKey(channelServer.getId()))
                st.append(translator.getLabel(language, "server.listing",
                        channelsMap.get(channelServer.getId()).getMention(), channelServer.getServer())).append("\n");

        return st.toString();
    }

    private String getAvailableServers(List<Server> serverList, Language language){
        return Optional.of(serverList.stream()
                .map(server -> translator.getLabel(language, server))
                .collect(Collectors.joining(", ")))
                .filter(Predicate.not(String::isEmpty))
                .orElse(translator.getLabel(language, "server.no_server", GAME.getName()));
    }
}