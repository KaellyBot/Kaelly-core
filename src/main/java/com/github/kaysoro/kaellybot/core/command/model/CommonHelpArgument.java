package com.github.kaysoro.kaellybot.core.command.model;

import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.PermissionScope;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;

import java.util.regex.Matcher;

public class CommonHelpArgument extends AbstractCommandArgument {

    public CommonHelpArgument(Command parent, Translator translator){
        super(parent, "\\s+help", false, PermissionScope.TEXT_PERMISSIONS, translator, Priority.HIGH);
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
        return message.getChannel()
                .flatMap(channel -> channel.createMessage(getParent().moreHelp(language, prefix)))
                .flatMapMany(Flux::just);
    }

    @Override
    public String help(Language lg, String prefix){
        return prefix + "`" + getParent().getName() + " help` : " + translator.getLabel(lg, "lambda.help");
    }
}
