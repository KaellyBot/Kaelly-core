package com.github.kaellybot.core.command.ping;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.command.util.AbstractCommandArgument;
import com.github.kaellybot.core.command.util.Command;
import com.github.kaellybot.core.util.DiscordTranslator;
import discord4j.core.object.command.Interaction;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.regex.Matcher;

@Component
@Qualifier(PingCommand.COMMAND_QUALIFIER)
public class PingArgument extends AbstractCommandArgument {

    public PingArgument(@Qualifier(PingCommand.COMMAND_QUALIFIER) Command parent, DiscordTranslator translator) {
        super(parent, translator);
    }

    @Override
    public Flux<Message> execute(Interaction interaction, Language language, Matcher matcher) {
        return interaction.getChannel()
                .flatMap(chan -> chan.createMessage(
                        Math.abs(Duration.between(interaction.getMessage().map(Message::getTimestamp)
                                .orElse(Instant.now()), Instant.now()).toMillis()) + "ms!"))
                .flatMapMany(Flux::just);
    }
}
