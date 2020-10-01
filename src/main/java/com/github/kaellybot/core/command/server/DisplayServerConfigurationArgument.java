package com.github.kaellybot.core.command.server;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.service.ServerService;
import com.github.kaellybot.core.command.model.Command;
import com.github.kaellybot.core.command.model.EmbedCommandArgument;
import com.github.kaellybot.core.mapper.ServerSnapshotMapper;
import com.github.kaellybot.core.model.constant.Constants;
import com.github.kaellybot.core.service.GuildService;
import com.github.kaellybot.core.util.DiscordTranslator;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;

@Component
@Qualifier(ServerCommand.COMMAND_QUALIFIER)
public class DisplayServerConfigurationArgument extends EmbedCommandArgument {

    private final GuildService guildService;

    private final ServerService serverService;

    private final ServerSnapshotMapper serverSnapshotMapper;

    public DisplayServerConfigurationArgument(@Qualifier(ServerCommand.COMMAND_QUALIFIER) Command parent,
                                              GuildService guildService, ServerService serverService,
                                              ServerSnapshotMapper serverSnapshotMapper, DiscordTranslator translator) {
        super(parent, true, translator);
        this.guildService = guildService;
        this.serverService = serverService;
        this.serverSnapshotMapper = serverSnapshotMapper;
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
        return Mono.zip(message.getGuild(),
                message.getGuild().flatMapMany(Guild::getChannels).collectList(),
                message.getGuildId().map(guildService::findById).orElse(Mono.empty()),
                translator.getServer(message),
                serverService.findAll().filter(server -> Constants.GAME.equals(server.getGame())).collectList())
                .flatMap(tuple -> message.getChannel()
                        .flatMap(channel -> channel.createEmbed(spec -> serverSnapshotMapper
                                .decorateSpec(spec, tuple.getT1(), tuple.getT2(), tuple.getT3(), tuple.getT4(),
                                        tuple.getT5(), language))))
                .flatMapMany(Flux::just);
    }
}