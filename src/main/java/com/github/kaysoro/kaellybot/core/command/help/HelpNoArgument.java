package com.github.kaysoro.kaellybot.core.command.help;

import com.github.kaysoro.kaellybot.core.command.model.TextCommandArgument;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.util.Translator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Qualifier(HelpCommand.COMMAND_QUALIFIER)
public class HelpNoArgument extends TextCommandArgument {

    public HelpNoArgument(HelpCommand parent, Translator translator) {
        super(parent, translator,
                message -> message.getChannel()
                        .zipWith(translator.getLanguage(message))
                        .flatMap(tuple -> tuple.getT1().createMessage(parent.getCommands().stream()
                                .filter(command -> command.isPublic() && ! command.isAdmin() && ! command.isHidden())
                                .map(command -> command.help(tuple.getT2(), Constants.DEFAULT_PREFIX))
                                .reduce((cmd1, cmd2) -> cmd1 + "\n" + cmd2)
                                .orElse(translator.getLabel(tuple.getT2(), "help.empty"))))
                        .flatMapMany(Flux::just)
        );
    }
}
