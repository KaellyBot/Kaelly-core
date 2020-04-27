package com.github.kaysoro.kaellybot.core.service;

import com.github.kaysoro.kaellybot.core.commands.model.Command;
import com.github.kaysoro.kaellybot.core.model.constants.Constants;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscordService {

    private DiscordClient discordClient;

    @Value("${discord.token}")
    private String token;

    private List<Command> commands;

    public DiscordService(List<Command> commands){
        this.commands = commands;
    }

    public void startBot(){
        if (discordClient == null){
            discordClient = DiscordClient.create(token);

            discordClient.withGateway(client -> {
                client.getEventDispatcher().on(ReadyEvent.class)
                        .flatMap(event -> event.getSelf().getClient()
                                .updatePresence(Presence.online(Activity.playing(Constants.GAME.getName()))))
                        .subscribe();

                client.getEventDispatcher().on(MessageCreateEvent.class)
                        .map(MessageCreateEvent::getMessage)
                        .subscribe(msg -> commands.forEach(cmd -> cmd.request(msg)));

                return client.onDisconnect();
            }).subscribe();
        }
    }
}