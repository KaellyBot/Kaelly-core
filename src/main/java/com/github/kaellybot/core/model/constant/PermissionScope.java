package com.github.kaellybot.core.model.constant;

import discord4j.rest.util.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Set;

import static discord4j.rest.util.Permission.*;

@Getter
@AllArgsConstructor
public enum PermissionScope {
    TEXT_PERMISSIONS(Set.of(SEND_MESSAGES, USE_EXTERNAL_EMOJIS)),
    EMBED_PERMISSIONS(Set.of(SEND_MESSAGES, USE_EXTERNAL_EMOJIS, ATTACH_FILES, EMBED_LINKS)),
    WEBHOOK_PERMISSIONS(Set.of(SEND_MESSAGES, USE_EXTERNAL_EMOJIS, MANAGE_WEBHOOKS)),

    MEMBER_PERMISSIONS(Collections.emptySet()),
    ADMINISTRATOR_PERMISSIONS(Set.of(Permission.MANAGE_GUILD));

    private Set<Permission> permissions;
}
