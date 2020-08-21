package com.github.kaysoro.kaellybot.core.command.help;

import com.github.kaysoro.kaellybot.core.command.model.TextCommandArgument;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.regex.Matcher;

@Component
@Qualifier(HelpCommand.COMMAND_QUALIFIER)
public class HelpNoArgument extends TextCommandArgument {

    public HelpNoArgument(HelpCommand parent, Translator translator) {
        super(parent, translator);
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Matcher matcher) {
        return message.getChannel()
                .zipWith(translator.getLanguage(message))
                .flatMap(tuple -> tuple.getT1().createMessage(((HelpCommand) getParent()).getCommands().stream()
                        .filter(command -> command.isPublic() && ! command.isAdmin() && ! command.isHidden())
                        .map(command -> command.help(tuple.getT2(), prefix))
                        .reduce((cmd1, cmd2) -> cmd1 + "\n" + cmd2)
                        .orElse(translator.getLabel(tuple.getT2(), "help.empty"))))
                .flatMapMany(Flux::just);
    }
}
