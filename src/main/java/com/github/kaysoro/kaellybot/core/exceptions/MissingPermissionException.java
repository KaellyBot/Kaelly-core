package com.github.kaysoro.kaellybot.core.exceptions;

import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.rest.util.Permission;
import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MissingPermissionException implements Exception {

    private final Command command;
    private final Set<Permission> permissions;

    public String getLabel(Translator translator, Language language){
        return permissions.stream()
                .map(permission -> translator.getLabel(language, "permission." + permission.name().toLowerCase()))
                .collect(Collectors.joining(", ", translator
                        .getLabel(language, "exception.missing_permission", command.getName()) + " ", "."));
    }
}
