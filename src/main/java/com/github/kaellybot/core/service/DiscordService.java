package com.github.kaellybot.core.service;

import com.github.kaellybot.core.command.util.Command;
import com.github.kaellybot.core.model.constant.Constants;
import com.github.kaellybot.core.trigger.Trigger;
import com.github.kaellybot.core.util.DiscordTranslator;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.guild.GuildDeleteEvent;
import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.event.domain.lifecycle.ReconnectEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.core.shard.MemberRequestFilter;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Service
public class DiscordService {

    private DiscordClient discordClient;

    @Value("${discord.token}")
    private String token;

    private final GuildService guildService;

    private final List<Command> commands;

    private final List<Trigger> triggers;

    private final DiscordTranslator translator;

    public DiscordService(GuildService guildService, List<Command> commands, List<Trigger> triggers, DiscordTranslator translator){
        this.guildService = guildService;
        this.commands = commands;
        this.triggers = triggers;
        this.translator = translator;
    }

    public void startBot(){
        if (discordClient == null){
            discordClient = DiscordClient.create(token);
            discordClient.gateway()
                    .setEnabledIntents(IntentSet.of(
                            Intent.GUILDS,
                            Intent.GUILD_MEMBERS,
                            Intent.GUILD_MESSAGES,
                            Intent.GUILD_MESSAGE_REACTIONS,
                            Intent.DIRECT_MESSAGES))
                    .setInitialPresence(ignored -> Presence.online(Activity.playing(Constants.GAME.getName().toUpperCase())))
                    .setMemberRequestFilter(MemberRequestFilter.none())
                    .withGateway(client -> Mono.when(
                        reconnectListener(client),
                        guildCreateListener(client),
                        guildDeleteListener(client),
                        commandListener(client),
                        triggerListener(client)))
                    .subscribe();
        }
    }

    private Mono<Void> commandListener(GatewayDiscordClient client){
        return client.getEventDispatcher().on(ApplicationCommandInteractionEvent.class)
                .map(ApplicationCommandInteractionEvent::getInteraction)
                .filter(interaction -> ! interaction.getUser().isBot())
                .flatMap(interaction -> translator.getLanguage(interaction)
                        .flatMapMany(language -> Flux.fromIterable(commands)
                                .flatMap(cmd -> cmd.request(interaction, language))))
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
                        .updatePresence(Presence.online(Activity.playing(Constants.GAME.getName().toUpperCase()))))
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