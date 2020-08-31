package com.github.kaysoro.kaellybot.core.command.almanaxauto;

import com.github.kaysoro.kaellybot.core.command.model.AbstractCommandArgument;
import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.model.entity.AlmanaxWebhook;
import com.github.kaysoro.kaellybot.core.service.AlmanaxWebhookService;
import com.github.kaysoro.kaellybot.core.util.PermissionScope;
import com.github.kaysoro.kaellybot.core.util.DiscordTranslator;
import com.github.kaysoro.kaellybot.core.mapper.WebhookMapper;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.regex.Matcher;

@Component
@Qualifier(AlmanaxAutoCommand.COMMAND_QUALIFIER)
public class AlmanaxAutoEnableArgument extends AbstractCommandArgument {

    private final AlmanaxWebhookService almanaxWebhookService;

    private final WebhookMapper webhookMapper;

    public AlmanaxAutoEnableArgument(@Qualifier(AlmanaxAutoCommand.COMMAND_QUALIFIER) Command parent, DiscordTranslator translator,
                                     AlmanaxWebhookService almanaxWebhookService, WebhookMapper webhookMapper) {
        super(parent, "\\s+(true|on|0)", true, PermissionScope.WEBHOOK_PERMISSIONS, translator);
        this.almanaxWebhookService = almanaxWebhookService;
        this.webhookMapper = webhookMapper;
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
        return message.getChannel().flatMapMany(chan -> ((TextChannel) chan).getWebhooks())
                .filterWhen(webhook -> almanaxWebhookService.existsById(webhook.getId()))
                .flatMap(webhook -> message.getChannel().flatMap(channel -> channel
                        .createMessage(translator.getLabel(language, "almanax-auto.enable_exist"))))
                .switchIfEmpty(message.getClient().getSelf().flatMap(self -> self.getAvatar()
                        .zipWith(message.getChannel().map(channel -> (TextChannel) channel))
                        .flatMap(tuple -> tuple.getT2().createWebhook(spec -> webhookMapper.decorateSpec(spec, self, tuple.getT1()))))
                        .flatMap(webhook -> almanaxWebhookService.save(AlmanaxWebhook.builder()
                                .webhookId(webhook.getId().asString())
                                .webhookToken(webhook.getToken())
                                .guildId(message.getGuildId().map(Snowflake::asString).orElse(null))
                                .language(Constants.DEFAULT_LANGUAGE)
                                .build()))
                        .flatMap(webhook -> message.getChannel().flatMap(channel -> channel
                                .createMessage(translator.getLabel(language, "almanax-auto.enable")))));
    }

    @Override
    public String help(Language lg, String prefix) {
        return prefix + "`" + getParent().getName() + " true` : " + translator.getLabel(lg, "almanax-auto.help.enable");
    }
}