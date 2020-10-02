package com.github.kaellybot.core.command.lang;

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
@Qualifier(LanguageCommand.COMMAND_QUALIFIER)
public class ResetChannelLanguageArgument extends EmbedCommandArgument {

    private final GuildService guildService;

    public ResetChannelLanguageArgument(@Qualifier(LanguageCommand.COMMAND_QUALIFIER) Command parent,
                                        GuildService guildService, DiscordTranslator translator) {
        super(parent, "\\s+-(c|chan|channel)\\s+-(r|reset)", true, translator);
        this.guildService = guildService;
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
        return message.getGuildId()
                        .map(guildService::findById)
                        .orElse(Mono.empty())
                        .map(guild -> resetChannelLanguage(guild, message.getChannelId().asString()))
                        .flatMap(guildService::update)
                        .flatMap(guild -> message.getChannel().flatMap(channel -> channel.createMessage(translator
                                .getLabel(guild.getLanguage(), "lang.updated"))))
                .flatMapMany(Flux::just);
    }

    private Guild resetChannelLanguage(Guild guild, String channelId){
        guild.getChannelLanguageList().removeIf(channelLanguage -> channelLanguage.getId().equals(channelId));
        return guild;
    }

    @Override
    public String help(Language lg, String prefix){
        return prefix + "`" + getParent().getName() + " -channel -reset` : " + translator.getLabel(lg, "lang.reset_channel_config");
    }
}