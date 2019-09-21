package com.github.kaysoro.kaellybot.core.commands.arguments.common;

import com.github.kaysoro.kaellybot.core.commands.arguments.model.AbstractCommandArgument;
import com.github.kaysoro.kaellybot.core.commands.model.Command;
import com.github.kaysoro.kaellybot.core.model.constants.DiscordConstants;
import com.github.kaysoro.kaellybot.core.model.constants.Language;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;

import java.util.regex.Matcher;

public class HelpArgument extends AbstractCommandArgument {

    public HelpArgument(Command parent){
        super(parent, "\\s+help", false);
    }

    @Override
    public void execute(Message message, Matcher matcher) {
        message.getChannel().flatMap(channel -> channel
                .createMessage(getParent().moreHelp(DiscordConstants.DEFAULT_LANGUAGE, DiscordConstants.DEFAULT_PREFIX)))
                .subscribe();
    }

    @Override
    public String help(Language lg, String prefix){
        return prefix + "`" + getParent().getName() + " help` : " + Translator.getLabel(lg, "lambda.help");
    }
}
