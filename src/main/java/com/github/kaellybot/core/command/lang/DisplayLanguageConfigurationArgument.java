package com.github.kaellybot.core.command.lang;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.command.model.Command;
import com.github.kaellybot.core.command.model.EmbedCommandArgument;
import com.github.kaellybot.core.mapper.LanguageSnapshotMapper;
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
@Qualifier(LanguageCommand.COMMAND_QUALIFIER)
public class DisplayLanguageConfigurationArgument extends EmbedCommandArgument {

    private final GuildService guildService;

    private final LanguageSnapshotMapper languageSnapshotMapper;

    public DisplayLanguageConfigurationArgument(@Qualifier(LanguageCommand.COMMAND_QUALIFIER) Command parent,
                                                GuildService guildService, LanguageSnapshotMapper languageSnapshotMapper,
                                                DiscordTranslator translator) {
        super(parent, true, translator);
        this.guildService = guildService;
        this.languageSnapshotMapper = languageSnapshotMapper;
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
        return Mono.zip(message.getGuild(),
                message.getGuild().flatMapMany(Guild::getChannels).collectList(),
                message.getGuildId().map(guildService::findById).orElse(Mono.empty()),
                message.getChannel())
                .flatMap(tuple -> tuple.getT4().createEmbed(spec -> languageSnapshotMapper
                        .decorateSpec(spec, tuple.getT1(), tuple.getT2(), tuple.getT3(), language)))
                .flatMapMany(Flux::just);
    }
}