package com.github.kaysoro.kaellybot.core.service;

import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.trigger.Trigger;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.guild.GuildDeleteEvent;
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
import java.util.function.Predicate;

@Service
public class DiscordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordService.class);

    private DiscordClient discordClient;

    @Value("${discord.token}")
    private String token;

    private final GuildService guildService;

    private final List<Command> commands;

    private final List<Trigger> triggers;

    public DiscordService(GuildService guildService, List<Command> commands, List<Trigger> triggers){
        this.guildService = guildService;
        this.commands = commands;
        this.triggers = triggers;
    }

    public void startBot(){
        if (discordClient == null){
            discordClient = DiscordClient.create(token);
            discordClient.withGateway(client -> Mono.when(
                    readyListener(client),
                    reconnectListener(client),
                    guildCreateListener(client),
                    guildDeleteListener(client),
                    commandListener(client),
                    triggerListener(client)))
                    .onErrorContinue((error, object) -> LOGGER.error("Error not managed: ", error))
                    .subscribe();
        }
    }

    private Mono<Void> readyListener(GatewayDiscordClient client){
        return client.getEventDispatcher().on(ReadyEvent.class)
                .flatMap(event -> event.getSelf().getClient()
                        .updatePresence(Presence.online(Activity.playing(Constants.GAME.getName()))))
                .then();
    }

    private Mono<Void> commandListener(GatewayDiscordClient client){
        return client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filterWhen(message -> message.getAuthorAsMember().map(member -> ! member.isBot()))
                .flatMap(msg -> Flux.fromIterable(commands).flatMap(cmd -> cmd.request(msg)))
                .then();
    }

    private Mono<Void> triggerListener(GatewayDiscordClient client){
        return client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .flatMap(msg -> Flux.fromIterable(triggers)
                        .filterWhen(trigger -> trigger.isTriggered(msg))
                        .flatMap(trigger -> trigger.execute(msg)))
                .then();
    }

    private Mono<Void> reconnectListener(GatewayDiscordClient client){
        return client.getEventDispatcher().on(ReconnectEvent.class)
                .flatMap(event -> event.getClient()
                        .updatePresence(Presence.online(Activity.playing(Constants.GAME.getName()))))
                .then();
    }

    private Mono<Void> guildCreateListener(GatewayDiscordClient client){
        return client.getEventDispatcher().on(GuildCreateEvent.class)
                .map(GuildCreateEvent::getGuild)
                .filterWhen(guild -> guildService.existsById(guild.getId()).map(found -> ! found))
                .flatMap(guildService::save)
                .then();
    }

    private Mono<Void> guildDeleteListener(GatewayDiscordClient client){
        return client.getEventDispatcher().on(GuildDeleteEvent.class)
                .filter(Predicate.not(GuildDeleteEvent::isUnavailable))
                .map(GuildDeleteEvent::getGuildId)
                .flatMap(guildService::deleteById)
                .then();
    }
}