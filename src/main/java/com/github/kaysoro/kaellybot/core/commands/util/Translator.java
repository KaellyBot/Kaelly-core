package com.github.kaysoro.kaellybot.core.commands.util;

import com.github.kaysoro.kaellybot.core.model.constants.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public final class Translator {

    private static final Logger LOG = LoggerFactory.getLogger(Translator.class);
    private static Map<Language, Properties> labels;

    private Translator(){}

    public static synchronized String getLabel(Language lang, String property){
        if (labels == null){
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

        String value = labels.get(lang).getProperty(property);
        if (value == null || value.trim().isEmpty()) {
            LOG.error("Missing label in {} : {}", lang, property);
            return property;
        }

        return value;
    }
}
