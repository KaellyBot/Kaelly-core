package com.github.kaellybot.core.command.almanaxauto;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.command.model.Command;
import com.github.kaellybot.core.service.AlmanaxWebhookService;
import com.github.kaellybot.core.util.PermissionScope;
import com.github.kaellybot.core.command.model.AbstractCommandArgument;
import com.github.kaellybot.core.util.DiscordTranslator;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.regex.Matcher;

@Component
@Qualifier(AlmanaxAutoCommand.COMMAND_QUALIFIER)
public class AlmanaxAutoDisableArgument extends AbstractCommandArgument {

    private final AlmanaxWebhookService almanaxWebhookService;

    public AlmanaxAutoDisableArgument(@Qualifier(AlmanaxAutoCommand.COMMAND_QUALIFIER) Command parent, DiscordTranslator translator,
                                      AlmanaxWebhookService almanaxWebhookService) {
        super(parent, "\\s+(false|off|1)", true, PermissionScope.WEBHOOK_PERMISSIONS, translator);
        this.almanaxWebhookService = almanaxWebhookService;
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
        return message.getChannel().flatMapMany(chan -> ((TextChannel) chan).getWebhooks())
                .flatMap(webhook -> almanaxWebhookService.deleteById(webhook.getId()).then(webhook.delete()))
                .then(message.getChannel())
                .flatMap(channel -> channel.createMessage(translator.getLabel(language, "almanax-auto.disable")))
                .flatMapMany(Flux::just);
    }

    @Override
    public String help(Language lg, String prefix) {
        return prefix + "`" + getParent().getName() + " false` : " + translator.getLabel(lg, "almanax-auto.help.disable");
    }
}