package com.github.kaellybot.core.command.server;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.service.ServerService;
import com.github.kaellybot.core.command.model.Command;
import com.github.kaellybot.core.command.model.EmbedCommandArgument;
import com.github.kaellybot.core.mapper.ServerSnapshotMapper;
import com.github.kaellybot.core.model.constant.Constants;
import com.github.kaellybot.core.util.DiscordTranslator;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.regex.Matcher;

@Component
@Qualifier(ServerCommand.COMMAND_QUALIFIER)
public class ServerArgument extends EmbedCommandArgument {

    private final ServerService serverService;

    private final ServerSnapshotMapper serverSnapshotMapper;

    public ServerArgument(@Qualifier(ServerCommand.COMMAND_QUALIFIER) Command parent, ServerService serverService,
                          ServerSnapshotMapper serverSnapshotMapper, DiscordTranslator translator) {
        super(parent, true, translator);
        this.serverService = serverService;
        this.serverSnapshotMapper = serverSnapshotMapper;
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
        return serverService.findAll()
                .filter(server -> Constants.GAME.equals(server.getGame()))
                .collectList()
                .flatMap(serverList -> message.getChannel()
                        .flatMap(channel -> channel.createEmbed(spec -> serverSnapshotMapper
                                .decorateSpec(spec, null, serverList, language))))
                .flatMapMany(Flux::just);
    }
}