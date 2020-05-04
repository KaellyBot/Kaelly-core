package com.github.kaysoro.kaellybot.core.util;

import discord4j.rest.util.Permission;

import java.util.Set;

public final class PermissionScope {

    private PermissionScope(){}

    public static final Set<Permission> EMBED_PERMISSIONS = Set.of(
            Permission.SEND_MESSAGES,
            Permission.ATTACH_FILES,
            Permission.EMBED_LINKS);

    public static final Set<Permission> TEXT_PERMISSIONS = Set.of(
            Permission.SEND_MESSAGES);
}
