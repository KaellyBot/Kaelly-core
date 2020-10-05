package com.github.kaellybot.core.command.server;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.command.model.Command;
import com.github.kaellybot.core.command.model.EmbedCommandArgument;
import com.github.kaellybot.core.model.entity.Guild;
import com.github.kaellybot.core.service.GuildService;
import com.github.kaellybot.core.util.DiscordTranslator;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;

@Component
@Qualifier(ServerCommand.COMMAND_QUALIFIER)
public class ResetChannelServerArgument extends EmbedCommandArgument {

    private final GuildService guildService;

    public ResetChannelServerArgument(@Qualifier(ServerCommand.COMMAND_QUALIFIER) Command parent,
                                      GuildService guildService, DiscordTranslator translator) {
        super(parent, "\\s+-(c|chan|channel)\\s+-(r|reset)", true, translator);
        this.guildService = guildService;
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
        return message.getGuildId()
                        .map(guildService::findById)
                        .orElse(Mono.empty())
                        .map(guild -> resetChannelServer(guild, message.getChannelId().asString()))
                        .flatMap(guildService::update)
                        .flatMap(guild -> message.getChannel().flatMap(channel -> channel.createMessage(translator
                                .getLabel(language, "server.updated"))))
                .flatMapMany(Flux::just);
    }

    private Guild resetChannelServer(Guild guild, String channelId){
        guild.getChannelServersList().removeIf(channelServer -> channelServer.getId().equals(channelId));
        return guild;
    }

    @Override
    public String help(Language lg, String prefix){
        return prefix + "`" + getParent().getName() + " -channel -reset` : " + translator.getLabel(lg, "server.reset_channel_config");
    }
}