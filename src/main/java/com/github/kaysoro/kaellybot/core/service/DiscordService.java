package com.github.kaysoro.kaellybot.core.service;

import com.github.kaysoro.kaellybot.core.commands.classic.PingCommand;
import com.github.kaysoro.kaellybot.core.commands.model.Command;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DiscordService implements IDiscordService{

    private final static Logger LOG = LoggerFactory.getLogger(DiscordService.class);

    private Flux<DiscordClient> discordClient;

    @Value("${discord.token}")
    private String token;

    @Override
    public void startBot(){
        if (discordClient == null){

            // TODO the following variable will be replaced by a bean dedicated to collect all declared commands
            List<Command> commands = Stream.of(new PingCommand()).collect(Collectors.toList());

            final DiscordClient client = new DiscordClientBuilder(token).build();

            client.getEventDispatcher().on(ReadyEvent.class)
                    .subscribe(ready -> LOG.info("Logged in as " + ready.getSelf().getUsername()));

            client.getEventDispatcher().on(MessageCreateEvent.class)
                    .map(MessageCreateEvent::getMessage)
                    .subscribe(msg -> commands.forEach(cmd -> cmd.request(msg)));

            client.login().block();
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