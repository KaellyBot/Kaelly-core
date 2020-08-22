package com.github.kaysoro.kaellybot.core.trigger;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Trigger {

    Mono<Boolean> isTriggered(Message message);

    Flux<Message> execute(Message message);
}
