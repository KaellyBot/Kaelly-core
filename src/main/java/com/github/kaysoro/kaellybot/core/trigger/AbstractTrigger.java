package com.github.kaysoro.kaellybot.core.trigger;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.PermissionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public abstract class AbstractTrigger implements Trigger {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTrigger.class);

    @Override
    public Mono<Boolean> isTriggered(Message message) {
        return message.getChannel()
                .filter(channel -> channel instanceof TextChannel)
                .map(TextChannel.class::cast)
                .zipWith(message.getClient().getSelfId())
                .flatMap(tuple -> tuple.getT1().getEffectivePermissions(tuple.getT2()))
                .map(permissions -> isTriggerHasPermissionsNeeded(permissions)
                        && isPatternFound(message.getContent()));
    }

    protected void manageUnknownException(Message message, Throwable error){
        LOGGER.error("Error with the following trigger: {}", message.getContent(), error);
    }

    protected abstract boolean isPatternFound(String content);

    protected abstract boolean isTriggerHasPermissionsNeeded(PermissionSet permissions);
}
