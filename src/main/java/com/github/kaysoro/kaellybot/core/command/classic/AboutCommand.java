package com.github.kaysoro.kaellybot.core.command.classic;

import com.github.kaysoro.kaellybot.core.command.argument.model.EmbedCommandArgument;
import com.github.kaysoro.kaellybot.core.command.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.model.constant.Donator;
import com.github.kaysoro.kaellybot.core.model.constant.Graphist;
import com.github.kaysoro.kaellybot.core.util.Translator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.stream.Stream;

@Component
public class AboutCommand extends AbstractCommand {

    public AboutCommand(Translator translator) {
        super("about", translator);

        getArguments().add(new EmbedCommandArgument(this, translator,
                message -> message.getChannel()
                        .flatMap(chan -> chan.createEmbed(spec -> spec
                                .setTitle(translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.title",
                                        Constants.NAME, Constants.VERSION))
                                .setDescription(translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.desc",
                                        Constants.GAME.getName()))
                                .setColor(Constants.COLOR)
                                .setThumbnail(Constants.AVATAR)
                                .setImage(Constants.CHANGELOG)
                                .addField(translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.invite.title"),
                                        translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.invite.desc",
                                                Constants.NAME, Constants.INVITE), true)
                                .addField(translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.support.title"),
                                        translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.support.desc",
                                                Constants.NAME, Constants.DISCORD_INVITE), true)
                                .addField(translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.twitter.title"),
                                        translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.twitter.desc",
                                                Constants.NAME, Constants.TWITTER), true)
                                .addField(translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.opensource.title"),
                                        translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.opensource.desc",
                                                Constants.GIT), true)
                                .addField(translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.free.title"),
                                        translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.free.desc",
                                                Constants.PAYPAL), true)
                                .addField(translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.graphist.title"),
                                        translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.graphist.desc",
                                                Graphist.ELYCANN.toMarkdown()), true)
                                .addField(translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.donators.title"),
                                                Stream.of(Donator.values()).map(Donator::getName)
                                                        .reduce((name1, name2) -> name1 + ", " + name2)
                                                        .orElse(translator.getLabel(Constants.DEFAULT_LANGUAGE,
                                                                "about.donators.empty")), true)))
                        .flatMapMany(Flux::just)
            )
        );
    }
}