package com.github.kaellybot.core.command.server;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.service.ServerService;
import com.github.kaellybot.core.command.util.AbstractCommandArgument;
import com.github.kaellybot.core.command.util.Command;
import com.github.kaellybot.core.mapper.ServerSnapshotMapper;
import com.github.kaellybot.core.model.constant.Constants;
import com.github.kaellybot.core.service.GuildService;
import com.github.kaellybot.core.util.annotation.BotPermissions;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.util.annotation.Described;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;

import static com.github.kaellybot.core.model.constant.PermissionScope.EMBED_PERMISSIONS;

@Component
@Qualifier(ServerCommand.COMMAND_QUALIFIER)
@BotPermissions(EMBED_PERMISSIONS)
@Described
public class DisplayServerConfigurationArgument extends AbstractCommandArgument {

    private final GuildService guildService;

    private final ServerService serverService;

    private final ServerSnapshotMapper serverSnapshotMapper;

    public DisplayServerConfigurationArgument(@Qualifier(ServerCommand.COMMAND_QUALIFIER) Command parent,
                                              GuildService guildService, ServerService serverService,
                                              ServerSnapshotMapper serverSnapshotMapper, DiscordTranslator translator) {
        super(parent, translator);
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

    @Override
    public String help(Language lg, String prefix){
        return prefix + "`" + getParent().getName() + "` : " + translator.getLabel(lg, "server.display_config");
    }
}