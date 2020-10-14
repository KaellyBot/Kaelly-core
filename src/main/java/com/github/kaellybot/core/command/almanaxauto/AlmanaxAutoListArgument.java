package com.github.kaellybot.core.command.almanaxauto;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.command.util.AbstractCommandArgument;
import com.github.kaellybot.core.command.util.Command;
import com.github.kaellybot.core.service.AlmanaxWebhookService;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.util.annotation.Described;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Component
@Qualifier(AlmanaxAutoCommand.COMMAND_QUALIFIER)
@Described
public class AlmanaxAutoListArgument extends AbstractCommandArgument {

    private final AlmanaxWebhookService almanaxWebhookService;

    public AlmanaxAutoListArgument(@Qualifier(AlmanaxAutoCommand.COMMAND_QUALIFIER) Command parent, DiscordTranslator translator,
                                   AlmanaxWebhookService almanaxWebhookService) {
        super(parent, translator);
        this.almanaxWebhookService = almanaxWebhookService;
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
        return  message.getGuildId().map(almanaxWebhookService::findAllByGuildId).orElse(Flux.empty())
                .flatMap(entity -> message.getClient().getWebhookById(Snowflake.of(entity.getWebhookId()))
                        .onErrorResume(error -> Mono.empty()))
                .flatMap(webhook -> message.getClient().getChannelById(webhook.getChannelId())
                        .onErrorResume(error -> Mono.empty()))
                .map(Channel::getMention)
                .collectList()
                .map(list -> getListLabel(language, list))
                .zipWith(message.getChannel())
                .flatMap(tuple -> tuple.getT2().createMessage(tuple.getT1()))
                .flatMapMany(Flux::just);
    }

    private String getListLabel(Language language, List<String> mentions){
        return mentions.isEmpty() ? translator.getLabel(language, "almanax-auto.list_empty") :
                translator.getLabel(language, "almanax-auto.list",
                        mentions.stream().collect(Collectors.joining("\n- ", "\n- ", "")));
    }

    @Override
    public String help(Language lg, String prefix) {
        return prefix + "`" + getParent().getName() + "` : " + translator.getLabel(lg, "almanax-auto.help.list");
    }
}