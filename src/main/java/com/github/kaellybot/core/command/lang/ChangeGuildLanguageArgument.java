package com.github.kaellybot.core.command.lang;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.service.LanguageService;
import com.github.kaellybot.core.command.util.AbstractCommandArgument;
import com.github.kaellybot.core.command.util.Command;
import com.github.kaellybot.core.model.constant.Order;
import com.github.kaellybot.core.model.entity.Guild;
import com.github.kaellybot.core.service.GuildService;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.util.annotation.Described;
import com.github.kaellybot.core.util.annotation.DisplayOrder;
import com.github.kaellybot.core.util.annotation.UserPermissions;
import discord4j.core.object.command.Interaction;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;

import static com.github.kaellybot.core.model.constant.PermissionScope.ADMINISTRATOR_PERMISSIONS;

@Component
@Qualifier(LanguageCommand.COMMAND_QUALIFIER)
@UserPermissions(ADMINISTRATOR_PERMISSIONS)
@DisplayOrder(Order.SECOND)
@Described
public class ChangeGuildLanguageArgument extends AbstractCommandArgument {

    private final GuildService guildService;

    private final LanguageService languageService;

    public ChangeGuildLanguageArgument(@Qualifier(LanguageCommand.COMMAND_QUALIFIER) Command parent,
                                       GuildService guildService, LanguageService languageService,
                                       DiscordTranslator translator) {
        super(parent, "\\s+(\\w+)", translator);
        this.guildService = guildService;
        this.languageService = languageService;
    }

    @Override
    public Flux<Message> execute(Interaction interaction, Language language, Matcher matcher) {
        return languageService.findByAbbreviation(matcher.group(1))
                .map(newLanguage -> interaction.getGuildId()
                        .map(guildService::findById)
                        .orElse(Mono.empty())
                        .map(guild -> changeLanguage(guild, newLanguage))
                        .flatMap(guildService::update)
                        .flatMap(guild -> interaction.getChannel().flatMap(channel -> channel.createMessage(translator
                                .getLabel(newLanguage, "lang.updated")))))
                .orElse(interaction.getChannel().flatMap(channel -> channel.createMessage(translator
                        .getLabel(language, "lang.not_found", matcher.group(1)))))
                .flatMapMany(Flux::just);
    }

    private Guild changeLanguage(Guild guild, Language language){
        guild.setLanguage(language);
        return guild;
    }

    @Override
    public String help(Language lg){
        return "`" + getParent().getName() + " FR` : " + translator.getLabel(lg, "lang.change_config");
    }
}