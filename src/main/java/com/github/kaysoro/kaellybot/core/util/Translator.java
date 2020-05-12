package com.github.kaysoro.kaellybot.core.util;

import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.model.constant.MultilingualEnum;
import com.github.kaysoro.kaellybot.core.model.entity.Guild;
import com.github.kaysoro.kaellybot.core.service.GuildService;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Translator {

    private static final Logger LOG = LoggerFactory.getLogger(Translator.class);

    private final GuildService guildService;

    private final Map<Language, Properties> labels;

    public Translator(GuildService guildService){
        this.guildService = guildService;
        labels = new ConcurrentHashMap<>();

        for(Language lg : Language.values())
            try(InputStream file = Translator.class.getResourceAsStream("/label_" + lg + ".properties")) {
                Properties prop = new Properties();
                prop.load(new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8)));
                labels.put(lg, prop);
            } catch (IOException e) {
                LOG.error("Translator.getLabel", e);
            }
    }

    public String getLabel(Language lang, MultilingualEnum enumeration){
        return getLabel(lang, enumeration.getKey());
    }

    public String getLabel(Language lang, String property, Object... arguments){
        String value = labels.get(lang).getProperty(property);
        if (value == null || value.trim().isEmpty()) {
            LOG.error("Missing label in {} : {}", lang, property);
            return property;
        }

        for(Object arg : arguments)
            value = value.replaceFirst("\\{}", arg.toString());

        return value;
    }

    public Mono<Language> getLanguage(Message message){
        return message.getGuild()
                .flatMap(guild -> guildService.findById(guild.getId()))
                .map(Guild::getLanguage)
                .defaultIfEmpty(Constants.DEFAULT_LANGUAGE);
    }
}
