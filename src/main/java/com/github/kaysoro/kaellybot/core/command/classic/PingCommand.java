package com.github.kaysoro.kaellybot.core.command.classic;

import com.github.kaysoro.kaellybot.core.command.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.command.argument.model.BasicCommandArgument;
import com.github.kaysoro.kaellybot.core.util.Translator;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class PingCommand extends AbstractCommand {

    public PingCommand(Translator translator) {
        super("ping", translator);

        getArguments().add(new BasicCommandArgument(this, translator,
                message -> message.getChannel()
                        .flatMap(chan -> chan.createMessage(
                                Math.abs(Duration.between(message.getTimestamp(), Instant.now()).toMillis()) + "ms!"))
                        .subscribe()));
    }
}