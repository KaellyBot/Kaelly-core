package com.github.kaysoro.kaellybot.core.trigger;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Set;

public abstract class AbstractTrigger implements Trigger {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTrigger.class);

    private Set<Permission> permissions;

    public AbstractTrigger(Set<Permission> permissions){
        this.permissions = permissions;
    }

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

    private boolean isTriggerHasPermissionsNeeded(PermissionSet permissions){
        return permissions.containsAll(this.permissions);
    }

    protected abstract boolean isPatternFound(String content);

    protected void manageUnknownException(Message message, Throwable error){
        LOGGER.error("Error with the following trigger: {}", message.getContent(), error);
    }
}
