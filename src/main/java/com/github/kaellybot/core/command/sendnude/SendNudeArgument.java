package com.github.kaellybot.core.command.sendnude;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.command.util.AbstractCommandArgument;
import com.github.kaellybot.core.command.util.Command;
import com.github.kaellybot.core.model.constant.Nude;
import com.github.kaellybot.core.util.annotation.BotPermissions;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.util.annotation.NSFW;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.regex.Matcher;

import static com.github.kaellybot.core.model.constant.PermissionScope.EMBED_PERMISSIONS;

@Component
@Qualifier(SendNudeCommand.COMMAND_QUALIFIER)
@NSFW
@BotPermissions(EMBED_PERMISSIONS)
public class SendNudeArgument extends AbstractCommandArgument {

    public SendNudeArgument(@Qualifier(SendNudeCommand.COMMAND_QUALIFIER) Command parent, DiscordTranslator translator) {
        super(parent, translator);
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
        return message.getChannel()
                .flatMap(channel -> channel.createEmbed(spec -> spec
                        .setTitle(translator.getLabel(language, "sendnude.title"))
                        .setFooter(translator.getLabel(language, "sendnude.author",
                                Nude.MOAM.getAuthor(), "1", "1"), null)
                        .setImage(Nude.MOAM.getImage())
                        .setColor(Color.of(16738740))))
                .flatMapMany(Flux::just);
    }
}