package com.github.kaysoro.kaellybot.core.commands.classic;

import com.github.kaysoro.kaellybot.core.commands.arguments.model.BasicCommandArgument;
import com.github.kaysoro.kaellybot.core.commands.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.model.constants.Constants;
import com.github.kaysoro.kaellybot.core.model.constants.Donator;
import com.github.kaysoro.kaellybot.core.util.Translator;

import java.util.stream.Stream;

public class DonateCommand extends AbstractCommand {

    public DonateCommand() {
        super("donate");

        getArguments().add(new BasicCommandArgument(this,
                message -> message.getChannel()
                        .flatMap(chan -> chan.createEmbed(spec -> {
                            spec.setTitle(Translator
                                    .getLabel(Constants.DEFAULT_LANGUAGE, "about.title")
                                    .replace("{name}", Constants.NAME)
                                    .replace("{version}", Constants.VERSION))
                                    .setDescription(Translator
                                            .getLabel(Constants.DEFAULT_LANGUAGE, "about.desc")
                                            .replace("{game}", Constants.GAME.getName()))
                                    .setColor(Constants.COLOR)
                                    .setThumbnail(Constants.AVATAR);
                            Stream.of(Donator.values())
                                    .forEach(donator -> spec.addField(donator.getName(), Translator
                                            .getLabel(Constants.DEFAULT_LANGUAGE, "donator."
                                                    + donator.name().toLowerCase() + ".desc"), false));
                        }))
                        .subscribe()));
    }
}