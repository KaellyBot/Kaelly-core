package com.github.kaysoro.kaellybot.core.commands.classic;

import com.github.kaysoro.kaellybot.core.commands.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.commands.model.AbstractCommandArgument;
import com.github.kaysoro.kaellybot.core.model.constants.DiscordConstants;
import com.github.kaysoro.kaellybot.core.model.constants.Language;
import discord4j.core.object.entity.Message;

public class TestCommand extends AbstractCommand {

    public TestCommand() {
        super("test");

        getArguments().add(new AbstractCommandArgument(this, "") {

            @Override
            public void execute(Message message) {
                message.getChannel()
                        .flatMap(chan -> chan.createMessage(parent
                                .moreHelp(Language.FR, DiscordConstants.DEFAULT_PREFIX)))
                        .subscribe();
            }
        });
    }
}