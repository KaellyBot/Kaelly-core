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
@DisplayOrder(Order.THIRD)
@Described
public class ChangeChannelLanguageArgument extends AbstractCommandArgument {

    private final GuildService guildService;

    private final LanguageService languageService;

    public ChangeChannelLanguageArgument(@Qualifier(LanguageCommand.COMMAND_QUALIFIER) Command parent,
                                         GuildService guildService, LanguageService languageService,
                                         DiscordTranslator translator) {
        super(parent, "\\s+-(c|chan|channel)\\s+(\\w+)", translator);
        this.guildService = guildService;
        this.languageService = languageService;
    }

    @Override
    public Flux<Message> execute(Interaction interaction, Language language, Matcher matcher) {
        return languageService.findByAbbreviation(matcher.group(2))
                .map(newLanguage -> interaction.getGuildId()
                        .map(guildService::findById)
                        .orElse(Mono.empty())
                        .map(guild -> changeChannelLanguage(guild, interaction.getChannelId().asString(), newLanguage))
                        .flatMap(guildService::update)
                        .flatMap(guild -> interaction.getChannel().flatMap(channel -> channel.createMessage(translator
                                .getLabel(newLanguage, "lang.updated")))))
                .orElse(interaction.getChannel().flatMap(channel -> channel.createMessage(translator
                        .getLabel(language, "lang.not_found", matcher.group(2)))))
                .flatMapMany(Flux::just);
    }

    private Guild changeChannelLanguage(Guild guild, String channelId, Language language){
        guild.getChannelLanguageList().removeIf(channelLanguage -> channelLanguage.getId().equals(channelId));
        guild.getChannelLanguageList().add(Guild.ChannelLanguage.builder().id(channelId).language(language).build());
        return guild;
    }

    @Override
    public String help(Language lg){
        return "`" + getParent().getName() + " -channel FR` : " + translator.getLabel(lg, "lang.change_channel_config");
    }
}