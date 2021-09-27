package com.github.kaellybot.core.command.almanaxauto;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.command.util.Command;
import com.github.kaellybot.core.mapper.WebhookMapper;
import com.github.kaellybot.core.model.constant.Constants;
import com.github.kaellybot.core.model.constant.Order;
import com.github.kaellybot.core.model.entity.AlmanaxWebhook;
import com.github.kaellybot.core.service.AlmanaxWebhookService;
import com.github.kaellybot.core.command.util.AbstractCommandArgument;
import com.github.kaellybot.core.util.annotation.BotPermissions;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.util.annotation.Described;
import com.github.kaellybot.core.util.annotation.DisplayOrder;
import com.github.kaellybot.core.util.annotation.UserPermissions;
import discord4j.common.util.Snowflake;
import discord4j.core.object.command.Interaction;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import org.apache.commons.lang.StringUtils;
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
@DisplayOrder(Order.SECOND)
@Described
public class AlmanaxAutoEnableArgument extends AbstractCommandArgument {

    private final AlmanaxWebhookService almanaxWebhookService;

    private final WebhookMapper webhookMapper;

    public AlmanaxAutoEnableArgument(@Qualifier(AlmanaxAutoCommand.COMMAND_QUALIFIER) Command parent, DiscordTranslator translator,
                                     AlmanaxWebhookService almanaxWebhookService, WebhookMapper webhookMapper) {
        super(parent, "\\s+(true|on|0)", translator);
        this.almanaxWebhookService = almanaxWebhookService;
        this.webhookMapper = webhookMapper;
    }

    @Override
    public Flux<Message> execute(Interaction interaction, Language language, Matcher matcher) {
        return interaction.getChannel().flatMapMany(chan -> ((TextChannel) chan).getWebhooks())
                .filterWhen(webhook -> almanaxWebhookService.existsById(webhook.getId()))
                .flatMap(webhook -> interaction.getChannel().flatMap(channel -> channel
                        .createMessage(translator.getLabel(language, "almanax-auto.enable_exist"))))
                .switchIfEmpty(interaction.getClient().getSelf().flatMap(self -> self.getAvatar()
                        .zipWith(interaction.getChannel().map(TextChannel.class::cast))
                        .flatMap(tuple -> tuple.getT2().createWebhook(spec -> webhookMapper.decorateSpec(spec, self, tuple.getT1()))))
                        .flatMap(webhook -> almanaxWebhookService.save(AlmanaxWebhook.builder()
                                .webhookId(webhook.getId().asString())
                                .webhookToken(webhook.getToken().orElse(StringUtils.EMPTY))
                                .guildId(interaction.getGuildId().map(Snowflake::asString).orElse(null))
                                .language(Constants.DEFAULT_LANGUAGE)
                                .build()))
                        .flatMap(webhook -> interaction.getChannel().flatMap(channel -> channel
                                .createMessage(translator.getLabel(language, "almanax-auto.enable")))));
    }

    @Override
    public String help(Language lg) {
        return "`" + getParent().getName() + " true` : " + translator.getLabel(lg, "almanax-auto.help.enable");
    }
}