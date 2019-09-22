package com.github.kaysoro.kaellybot.core.commands.classic;

import com.github.kaysoro.kaellybot.core.commands.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.commands.arguments.model.BasicCommandArgument;

import java.time.Duration;
import java.time.Instant;

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