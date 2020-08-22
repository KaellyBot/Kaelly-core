package com.github.kaysoro.kaellybot.core.command.about;

import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.command.model.EmbedCommandArgument;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.model.constant.Donator;
import com.github.kaysoro.kaellybot.core.model.constant.Graphist;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.regex.Matcher;
import java.util.stream.Stream;

@Component
@Qualifier(AboutCommand.COMMAND_QUALIFIER)
public class AboutArgument extends EmbedCommandArgument {

    public AboutArgument(@Qualifier(AboutCommand.COMMAND_QUALIFIER) Command parent, Translator translator) {
        super(parent, translator);
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
        return message.getChannel()
                .flatMap(channel -> channel.createEmbed(spec -> spec
                        .setTitle(translator.getLabel(language, "about.title",
                                Constants.NAME, Constants.VERSION))
                        .setDescription(translator.getLabel(language, "about.desc",
                                Constants.GAME.getName()))
                        .setColor(Color.of(Constants.COLOR))
                        .setThumbnail(Constants.AVATAR)
                        .setImage(Constants.CHANGELOG)
                        .addField(translator.getLabel(language, "about.invite.title"),
                                translator.getLabel(language, "about.invite.desc",
                                        Constants.NAME, Constants.INVITE), true)
                        .addField(translator.getLabel(language, "about.support.title"),
                                translator.getLabel(language, "about.support.desc",
                                        Constants.NAME, Constants.DISCORD_INVITE), true)
                        .addField(translator.getLabel(language, "about.twitter.title"),
                                translator.getLabel(language, "about.twitter.desc",
                                        Constants.NAME, Constants.TWITTER), true)
                        .addField(translator.getLabel(language, "about.opensource.title"),
                                translator.getLabel(language, "about.opensource.desc",
                                        Constants.GIT), true)
                        .addField(translator.getLabel(language, "about.free.title"),
                                translator.getLabel(language, "about.free.desc",
                                        Constants.PAYPAL), true)
                        .addField(translator.getLabel(language, "about.graphist.title"),
                                translator.getLabel(language, "about.graphist.desc",
                                        Graphist.ELYCANN.toMarkdown()), true)
                        .addField(translator.getLabel(language, "about.donators.title"),
                                Stream.of(Donator.values()).map(Donator::getName)
                                        .reduce((name1, name2) -> name1 + ", " + name2)
                                        .orElse(translator.getLabel(language,
                                                "about.donators.empty")), true)))
                .flatMapMany(Flux::just);
    }
}
