package com.github.kaysoro.kaellybot.core.commands.classic;

import com.github.kaysoro.kaellybot.core.commands.arguments.model.BasicCommandArgument;
import com.github.kaysoro.kaellybot.core.commands.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.model.constants.Constants;
import com.github.kaysoro.kaellybot.core.model.constants.Graphist;
import com.github.kaysoro.kaellybot.core.util.Translator;

public class AboutCommand extends AbstractCommand {

    public AboutCommand() {
        super("about");

        getArguments().add(new BasicCommandArgument(this,
                (client, message) -> message.getChannel()
                        .flatMap(chan -> chan.createEmbed(spec -> spec.setTitle(Translator
                                .getLabel(Constants.DEFAULT_LANGUAGE, "about.title")
                                .replace("{name}", Constants.NAME)
                                .replace("{version}", Constants.VERSION))
                                .setDescription(Translator
                                        .getLabel(Constants.DEFAULT_LANGUAGE, "about.desc")
                                        .replace("{game}", Constants.GAME.getName()))
                                .setColor(Constants.COLOR)
                                .setThumbnail(Constants.AVATAR)
                                .setImage(Constants.CHANGELOG)
                                .addField(Translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.invite.title"), 
                                        Translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.invite.desc")
                                                .replace("{name}", Constants.NAME)
                                                .replace("{invite}", Constants.INVITE), true)
                                .addField(Translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.support.title"),
                                        Translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.support.desc")
                                                .replace("{name}", Constants.NAME)
                                                .replace("{discordInvite}", Constants.DISCORD_INVITE), true)
                                .addField(Translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.twitter.title"),
                                        Translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.twitter.desc")
                                                .replace("{name}", Constants.NAME)
                                                .replace("{twitter}", Constants.TWITTER), true)
                                .addField(Translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.opensource.title"),
                                        Translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.opensource.desc")
                                                .replace("{git}", Constants.GIT), true)
                                .addField(Translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.free.title"),
                                        Translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.free.desc")
                                                .replace("{paypal}", Constants.PAYPAL), true)
                                .addField(Translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.graphist.title"),
                                        Translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.graphist.desc")
                                                .replace("{graphist}", Graphist.ELYCANN.toMarkdown()), true)))
                        .subscribe()));
    }
}