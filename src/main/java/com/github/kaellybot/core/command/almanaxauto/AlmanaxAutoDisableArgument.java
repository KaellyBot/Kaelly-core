package com.github.kaellybot.core.command.almanaxauto;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.command.util.Command;
import com.github.kaellybot.core.model.constant.Order;
import com.github.kaellybot.core.service.AlmanaxWebhookService;
import com.github.kaellybot.core.command.util.AbstractCommandArgument;
import com.github.kaellybot.core.util.annotation.BotPermissions;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.util.annotation.Described;
import com.github.kaellybot.core.util.annotation.DisplayOrder;
import com.github.kaellybot.core.util.annotation.UserPermissions;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.regex.Matcher;

import static com.github.kaellybot.core.model.constant.PermissionScope.ADMINISTRATOR_PERMISSIONS;
import static com.github.kaellybot.core.model.constant.PermissionScope.WEBHOOK_PERMISSIONS;

@Component
@Qualifier(AlmanaxAutoCommand.COMMAND_QUALIFIER)
@BotPermissions(WEBHOOK_PERMISSIONS)
@UserPermissions(ADMINISTRATOR_PERMISSIONS)
@DisplayOrder(Order.THIRD)
@Described
public class AlmanaxAutoDisableArgument extends AbstractCommandArgument {

    private final AlmanaxWebhookService almanaxWebhookService;

    public AlmanaxAutoDisableArgument(@Qualifier(AlmanaxAutoCommand.COMMAND_QUALIFIER) Command parent, DiscordTranslator translator,
                                      AlmanaxWebhookService almanaxWebhookService) {
        super(parent, "\\s+(false|off|1)", translator);
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