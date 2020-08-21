package com.github.kaysoro.kaellybot.core.model.error;

import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.rest.util.Permission;
import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MissingPermissionError implements Error {

    private final Command command;
    private final Set<Permission> permissions;

    public String getLabel(Translator translator, Language language){
        return permissions.stream()
                .map(permission -> translator.getLabel(language, "permission." + permission.name().toLowerCase()))
                .collect(Collectors.joining(", ", translator
                        .getLabel(language, "error.missing_permission", command.getName()) + " ", "."));
    }
}
