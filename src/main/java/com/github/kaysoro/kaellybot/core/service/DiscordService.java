package com.github.kaysoro.kaellybot.core.service;

import com.github.kaysoro.kaellybot.core.commands.factory.CommandFactory;
import com.github.kaysoro.kaellybot.core.model.constants.Constants;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.core.shard.ShardingClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class DiscordService implements IDiscordService{

    private Flux<DiscordClient> discordClients;

    @Value("${discord.token}")
    private String token;

    private CommandFactory commandFactory;

    public DiscordService(CommandFactory commandFactory){
        this.commandFactory = commandFactory;
    }

    @Override
    public void startBot(){
        if (discordClients == null){
            discordClients = new ShardingClientBuilder(token)
                    .build()
                    .map(DiscordClientBuilder::build)
                    .cache();

            discordClients.flatMap(client -> client.getEventDispatcher().on(ReadyEvent.class))
                    .map(event -> event.getSelf().getClient()
                            .updatePresence(Presence.online(Activity.playing(Constants.GAME.getName()))))
                    .subscribe();
                    //.subscribe(allShardsFullyReady -> System.out.println("All shards fully ready"));

            discordClients.flatMap(client -> client.getEventDispatcher().on(MessageCreateEvent.class))
                    .map(MessageCreateEvent::getMessage)
                    .subscribe(msg -> commandFactory.getCommands().forEach(cmd -> cmd.request(msg)));

            discordClients.flatMap(DiscordClient::login)
                    .subscribe();
        }
    }
}