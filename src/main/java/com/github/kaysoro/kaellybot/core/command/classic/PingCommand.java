package com.github.kaysoro.kaellybot.core.command.classic;

import com.github.kaysoro.kaellybot.core.command.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.command.argument.model.BasicCommandArgument;
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
                                Math.abs(Duration.between(message.getTimestamp(), Instant.now()).toMillis()) + "ms!"))
                        .subscribe()));
    }
}