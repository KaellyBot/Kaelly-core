package com.github.kaysoro.kaellybot.core.command.classic;

import com.github.kaysoro.kaellybot.core.command.argument.model.BasicCommandArgument;
import com.github.kaysoro.kaellybot.core.command.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.model.constant.Donator;
import com.github.kaysoro.kaellybot.core.util.Translator;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class DonateCommand extends AbstractCommand {

    public DonateCommand(Translator translator) {
        super("donate", translator);

        getArguments().add(new BasicCommandArgument(this, translator,
                message -> message.getChannel()
                        .flatMap(chan -> chan.createEmbed(spec -> {
                            spec
                                .setTitle(translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.title",
                                        Constants.NAME, Constants.VERSION))
                                .setDescription(translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.desc", Constants.GAME.getName()))
                                .setColor(Constants.COLOR)
                                .setThumbnail(Constants.AVATAR);

                            Stream.of(Donator.values())
                                .forEach(donator -> spec.addField(donator.getName(), translator
                                        .getLabel(Constants.DEFAULT_LANGUAGE, "donator."
                                                + donator.name().toLowerCase() + ".desc"), false));
                        }))
                        .subscribe()));
    }
}