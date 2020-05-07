package com.github.kaysoro.kaellybot.core.service;

import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.trigger.Trigger;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.lifecycle.ReconnectEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class DiscordService {

    private Logger LOGGER = LoggerFactory.getLogger(DiscordService.class);
    private DiscordClient discordClient;

    @Value("${discord.token}")
    private String token;

    private List<Command> commands;

    private List<Trigger> triggers;

    public DiscordService(List<Command> commands, List<Trigger> triggers){
        this.commands = commands;
        this.triggers = triggers;
    }

    public void startBot(){
        if (discordClient == null){
            discordClient = DiscordClient.create(token);

            discordClient.withGateway(client -> {

                Mono<Void> triggerListener = client.getEventDispatcher().on(MessageCreateEvent.class)
                        .map(MessageCreateEvent::getMessage)
                        .flatMap(msg -> Flux.fromIterable(triggers)
                                .filterWhen(trigger -> trigger.isTriggered(msg))
                                .flatMap(trigger -> trigger.execute(msg)))
                        .then();

                return Mono.when(triggerListener);
            }).onErrorContinue((error, object) -> LOGGER.error("Error not managed: ", error)).subscribe();
        }
    }
}