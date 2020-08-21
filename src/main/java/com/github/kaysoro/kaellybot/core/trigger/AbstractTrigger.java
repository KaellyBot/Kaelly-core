package com.github.kaysoro.kaellybot.core.trigger;

import com.github.kaysoro.kaellybot.core.util.Translator;
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

    protected final Translator translator;

    private final Set<Permission> permissions;

    public AbstractTrigger(Translator translator, Set<Permission> permissions){
        this.translator = translator;
        this.permissions = permissions;
    }

    @Override
    public Mono<Boolean> isTriggered(Message message) {
        return message.getChannel()
                .filter(channel -> channel instanceof TextChannel)
                .map(TextChannel.class::cast)
                .flatMap(channel -> channel.getEffectivePermissions(message.getClient().getSelfId()))
                .map(channelPermissions -> isTriggerHasPermissionsNeeded(channelPermissions)
                        && isPatternFound(message.getContent()));
    }

    private boolean isTriggerHasPermissionsNeeded(PermissionSet permissions){
        return permissions.containsAll(this.permissions);
    }

    protected Mono<Message> manageUnknownException(Message message, Throwable error){
        LOGGER.error("Error with the following trigger: {}", message.getContent(), error);
        return Mono.empty();
    }

    protected abstract boolean isPatternFound(String content);
}
