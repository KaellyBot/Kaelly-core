package com.github.kaellybot.core.mapper;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.model.entity.Server;
import com.github.kaellybot.core.model.constant.Constants;
import com.github.kaellybot.core.model.entity.Guild;
import com.github.kaellybot.core.util.DiscordTranslator;
import discord4j.core.spec.EmbedCreateSpec;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ServerSnapshotMapper {

    private final DiscordTranslator translator;

    public void decorateSpec(EmbedCreateSpec spec, Guild guild, List<Server> serverList,
                             Language language){
        spec.setTitle("Server configuration")
                .setFooter(Constants.GAME.getName(), Constants.GAME.getIcon())
                .addField("Available servers", Optional.of(serverList.stream()
                        .map(server -> translator.getLabel(language, server))
                        .collect(Collectors.joining(", ")))
                        .filter(Predicate.not(String::isEmpty))
                        .orElse(translator.getLabel(language, "server.no_server")), true);
    }
}