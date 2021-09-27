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
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.core.shard.MemberRequestFilter;
import discord4j.discordjson.json.ApplicationCommandData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;
import discord4j.rest.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Service
public class DiscordService {

    private static final Logger LOG = LoggerFactory.getLogger(DiscordService.class);

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
                    .setInitialPresence(ignored -> ClientPresence.online(ClientActivity.playing(Constants.GAME.getName().toUpperCase())))
                    .setMemberRequestFilter(MemberRequestFilter.none())
                    .withGateway(client -> Mono.when(
                        reconnectListener(client),
                        guildCreateListener(client),
                        guildDeleteListener(client),
                        commandListener(client),
                        triggerListener(client)))
                    .subscribe();

            registerCommands(discordClient);
        }
    }

    private Mono<Void> commandListener(GatewayDiscordClient client){
        return client.getEventDispatcher().on(ApplicationCommandInteractionEvent.class)
                .filter(event -> ! event.getInteraction().getUser().isBot())
                .flatMap(event -> translator.getLanguage(event.getInteraction())
                        .flatMapMany(language -> Flux.fromIterable(commands)
                                .flatMap(cmd -> cmd.request(event, language))))
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
                        .updatePresence(ClientPresence.online(ClientActivity.playing(Constants.GAME.getName().toUpperCase()))))
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

    private void registerCommands(DiscordClient client){
        ApplicationService applicationService = client.getApplicationService();
        final long applicationId = discordClient.getApplicationId().block();
        Map<String, ApplicationCommandData> discordCommands = applicationService
                .getGlobalApplicationCommands(applicationId)
                .collectMap(ApplicationCommandData::name)
                .blockOptional().orElse(Collections.emptyMap());
        Map<String, ApplicationCommandRequest> localCommands = new HashMap<>();
        for(Command command : commands){
            localCommands.put(command.getName(), command.getApplicationCommandRequest());
            if (! discordCommands.containsKey(command.getName())) {
                applicationService.createGlobalApplicationCommand(applicationId, localCommands.get(command.getName())).block();
                LOG.info("Created global command: {}", command.getName());
            }
        }

        for (ApplicationCommandData discordCommand : discordCommands.values()) {
            long discordCommandId = Long.parseLong(discordCommand.id());
            ApplicationCommandRequest command = localCommands.get(discordCommand.name());

            if (command == null) {
                applicationService.deleteGlobalApplicationCommand(applicationId, discordCommandId).block();
                LOG.info("Deleted global command: {}", discordCommand.name());
                continue;
            }

            if (hasChanged(discordCommand, command)) {
                applicationService.modifyGlobalApplicationCommand(applicationId, discordCommandId, command).block();
                LOG.info("Updated global command: {}", command.name());
            }
        }
    }

    private boolean hasChanged(ApplicationCommandData discordCommand, ApplicationCommandRequest command) {
        boolean isDescriptionChanged = !command.description().toOptional()
                .map(desc -> desc.equals(discordCommand.description()))
                .orElse(true);
        if (isDescriptionChanged) return true;

        boolean isPermissionChanged = ! discordCommand.defaultPermission().toOptional().orElse(true)
                .equals(command.defaultPermission().toOptional().orElse(true));
        if (isPermissionChanged) return true;

        return !discordCommand.options().equals(command.options());
    }
}