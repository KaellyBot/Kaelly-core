package com.github.kaysoro.kaellybot.core.commands.classic;

import com.github.kaysoro.kaellybot.core.commands.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.commands.model.BasicCommandArgument;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class PingCommand extends AbstractCommand {

    public PingCommand() {
        super("ping");

        getArguments().add(new BasicCommandArgument(this,
                message -> message.getChannel()
                        .flatMap(chan -> chan.createMessage(
                                Duration.between(message.getTimestamp(), Instant.now()).toMillis() + "ms!"))
                        .subscribe()));
    }
}