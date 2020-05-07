package com.github.kaysoro.kaellybot.core.command.argument.common;

import com.github.kaysoro.kaellybot.core.command.argument.model.AbstractCommandArgument;
import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.PermissionScope;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;

import java.util.regex.Matcher;

public class HelpArgument extends AbstractCommandArgument {

    public HelpArgument(Command parent, Translator translator){
        super(parent, "\\s+help", false, PermissionScope.TEXT_PERMISSIONS, translator);
    }

    @Override
    public Flux<Message> execute(Message message, Matcher matcher) {
        return message.getChannel().flatMap(channel -> channel
                .createMessage(getParent().moreHelp(Constants.DEFAULT_LANGUAGE, Constants.DEFAULT_PREFIX)))
                .flatMapMany(Flux::just);
    }

    @Override
    public String help(Language lg, String prefix){
        return prefix + "`" + getParent().getName() + " help` : " + translator.getLabel(lg, "lambda.help");
    }
}