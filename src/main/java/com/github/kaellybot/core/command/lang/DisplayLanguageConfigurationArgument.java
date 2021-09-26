package com.github.kaellybot.core.command.lang;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.command.util.AbstractCommandArgument;
import com.github.kaellybot.core.command.util.Command;
import com.github.kaellybot.core.mapper.LanguageSnapshotMapper;
import com.github.kaellybot.core.service.GuildService;
import com.github.kaellybot.core.util.annotation.BotPermissions;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.util.annotation.Described;
import discord4j.core.object.command.Interaction;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;

import static com.github.kaellybot.core.model.constant.PermissionScope.EMBED_PERMISSIONS;

@Component
@Qualifier(LanguageCommand.COMMAND_QUALIFIER)
@BotPermissions(EMBED_PERMISSIONS)
@Described
public class DisplayLanguageConfigurationArgument extends AbstractCommandArgument {

    private final GuildService guildService;

    private final LanguageSnapshotMapper languageSnapshotMapper;

    public DisplayLanguageConfigurationArgument(@Qualifier(LanguageCommand.COMMAND_QUALIFIER) Command parent,
                                                GuildService guildService, LanguageSnapshotMapper languageSnapshotMapper,
                                                DiscordTranslator translator) {
        super(parent, translator);
        this.guildService = guildService;
        this.languageSnapshotMapper = languageSnapshotMapper;
    }

    @Override
    public Flux<Message> execute(Interaction interaction, Language language, Matcher matcher) {
        return Mono.zip(interaction.getGuild(),
                        interaction.getGuild().flatMapMany(Guild::getChannels).collectList(),
                        interaction.getGuildId().map(guildService::findById).orElse(Mono.empty()),
                        interaction.getChannel())
                .flatMap(tuple -> tuple.getT4().createEmbed(spec -> languageSnapshotMapper
                        .decorateSpec(spec, tuple.getT1(), tuple.getT2(), tuple.getT3(), language)))
                .flatMapMany(Flux::just);
    }

    @Override
    public String help(Language lg){
        return "`" + getParent().getName() + "` : " + translator.getLabel(lg, "lang.display_config");
    }
}