package com.github.kaellybot.core.command.prefix;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.command.util.AbstractCommandArgument;
import com.github.kaellybot.core.command.util.Command;
import com.github.kaellybot.core.model.constant.Constants;
import com.github.kaellybot.core.model.entity.Guild;
import com.github.kaellybot.core.service.GuildService;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.util.annotation.Described;
import com.github.kaellybot.core.util.annotation.UserPermissions;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;

import static com.github.kaellybot.core.model.constant.PermissionScope.ADMINISTRATOR_PERMISSIONS;

@Component
@Qualifier(PrefixCommand.COMMAND_QUALIFIER)
@UserPermissions(ADMINISTRATOR_PERMISSIONS)
@Described
public class ChangeGuildPrefixArgument extends AbstractCommandArgument {

    private final GuildService guildService;

    public ChangeGuildPrefixArgument(@Qualifier(PrefixCommand.COMMAND_QUALIFIER) Command parent,
                                     GuildService guildService, DiscordTranslator translator) {
        super(parent, "\\s+(.+)", translator);
        this.guildService = guildService;
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
        String newPrefix = matcher.group(1).trim();
        return Mono.just(validatePrefix(newPrefix))
                .filter(Boolean.TRUE::equals)
                .flatMap(value -> message.getGuildId().map(guildService::findById).orElse(Mono.empty()))
                .map(guild -> changePrefix(guild, newPrefix))
                .flatMap(guildService::update)
                .flatMap(guild -> message.getChannel().flatMap(channel -> channel.createMessage(translator
                        .getLabel(language, "prefix.updated", newPrefix))))
                .switchIfEmpty(message.getChannel().flatMap(channel -> channel.createMessage(translator
                        .getLabel(language, "prefix.restriction", newPrefix, Constants.PREFIX_MAXIMUM_LENGTH))))
                .flatMapMany(Flux::just);
    }

    public boolean validatePrefix(String prefix){
        return prefix.length() >= 1 && prefix.length() <= Constants.PREFIX_MAXIMUM_LENGTH;
    }

    private Guild changePrefix(Guild guild, String prefix){
        guild.setPrefix(prefix);
        return guild;
    }

    @Override
    public String help(Language lg, String prefix){
        return prefix + "`" + getParent().getName() + " !` : " + translator
                .getLabel(lg, "prefix.change_config", Constants.PREFIX_MAXIMUM_LENGTH);
    }
}