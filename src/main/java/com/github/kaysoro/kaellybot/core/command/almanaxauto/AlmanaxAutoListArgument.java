package com.github.kaysoro.kaellybot.core.command.almanaxauto;

import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.command.model.TextCommandArgument;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.service.AlmanaxWebhookService;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Component
@Qualifier(AlmanaxAutoCommand.COMMAND_QUALIFIER)
public class AlmanaxAutoListArgument extends TextCommandArgument {

    private final AlmanaxWebhookService almanaxWebhookService;

    public AlmanaxAutoListArgument(@Qualifier(AlmanaxAutoCommand.COMMAND_QUALIFIER) Command parent, Translator translator,
                                   AlmanaxWebhookService almanaxWebhookService) {
        super(parent, true, translator);
        this.almanaxWebhookService = almanaxWebhookService;
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
        return  message.getGuildId().map(almanaxWebhookService::findAllByGuildId).orElse(Flux.empty())
                .flatMap(webhook -> message.getClient().getChannelById(Snowflake.of(webhook.getChannelId())))
                .onErrorResume(e -> Flux.empty())
                .map(Channel::getMention)
                .collectList()
                .map(list -> translator.getLabel(language, "almanax-auto.list",
                        list.stream().collect(Collectors.joining("\n- ", "\n- ", ""))))
                .zipWith(message.getChannel())
                .flatMap(tuple -> tuple.getT2().createMessage(tuple.getT1()))
                .flatMapMany(Flux::just);
    }

    @Override
    public String help(Language lg, String prefix) {
        return prefix + "`" + getParent().getName() + "` : " + translator.getLabel(lg, "almanax-auto.list.help");
    }
}