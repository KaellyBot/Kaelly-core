package com.github.kaysoro.kaellybot.core.service;

import com.github.kaysoro.kaellybot.core.commands.factory.CommandFactory;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class DiscordService implements IDiscordService{

    private Flux<DiscordClient> discordClient;

    @Value("${discord.token}")
    private String token;

    private CommandFactory commandFactory;

    public DiscordService(CommandFactory commandFactory){
        this.commandFactory = commandFactory;
    }

    @Override
    public void startBot(){
        if (discordClient == null){
            final DiscordClient client = new DiscordClientBuilder(token).build();

            client.getEventDispatcher().on(ReadyEvent.class)
                    .subscribe(ready -> ready.getSelf().getClient()
                            .updatePresence(Presence.online(Activity.playing("Dofus"))));

            client.getEventDispatcher().on(MessageCreateEvent.class)
                    .map(MessageCreateEvent::getMessage)
                    .subscribe(msg -> commandFactory.getCommands().forEach(cmd -> cmd.request(msg)));

            client.login().subscribe();
            /**
             new ShardingClientBuilder(token)
                    .build()
                    .map(DiscordClientBuilder::build)
                    .flatMap(DiscordClient::login)
                    .subscribe();
             **/
        }
    }
}