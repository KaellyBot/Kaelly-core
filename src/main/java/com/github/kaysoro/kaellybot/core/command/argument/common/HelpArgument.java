package com.github.kaysoro.kaellybot.core.command.argument.common;

import com.github.kaysoro.kaellybot.core.command.argument.model.AbstractCommandArgument;
import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
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
                .createMessage(getParent().moreHelp(Constants.DEFAULT_LANGUAGE, Constants.DEFAULT_PREFIX)))
                .subscribe();
    }

    @Override
    public String help(Language lg, String prefix){
        return prefix + "`" + getParent().getName() + " help` : " + Translator.getLabel(lg, "lambda.help");
    }
}
