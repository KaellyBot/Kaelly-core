package com.github.kaellybot.core.command.lang;

import com.github.kaellybot.commons.model.constants.Language;
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
@DisplayOrder(Order.FOURTH)
@Described
public class ResetChannelLanguageArgument extends AbstractCommandArgument {

    private final GuildService guildService;

    public ResetChannelLanguageArgument(@Qualifier(LanguageCommand.COMMAND_QUALIFIER) Command parent,
                                        GuildService guildService, DiscordTranslator translator) {
        super(parent, "\\s+-(c|chan|channel)\\s+-(r|reset)", translator);
        this.guildService = guildService;
    }

    @Override
    public Flux<Message> execute(Interaction interaction, Language language, Matcher matcher) {
        return interaction.getGuildId()
                        .map(guildService::findById)
                        .orElse(Mono.empty())
                        .map(guild -> resetChannelLanguage(guild, interaction.getChannelId().asString()))
                        .flatMap(guildService::update)
                        .flatMap(guild -> interaction.getChannel().flatMap(channel -> channel.createMessage(translator
                                .getLabel(guild.getLanguage(), "lang.updated"))))
                .flatMapMany(Flux::just);
    }

    private Guild resetChannelLanguage(Guild guild, String channelId){
        guild.getChannelLanguageList().removeIf(channelLanguage -> channelLanguage.getId().equals(channelId));
        return guild;
    }

    @Override
    public String help(Language lg){
        return "`" + getParent().getName() + " -channel -reset` : " + translator.getLabel(lg, "lang.reset_channel_config");
    }
}