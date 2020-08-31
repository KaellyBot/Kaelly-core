package com.github.kaysoro.kaellybot.core.service;

import com.github.kaysoro.kaellybot.core.model.constant.Dimension;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.DiscordTranslator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class DimensionService {

    private final DiscordTranslator translator;

    public DimensionService(DiscordTranslator translator){
        this.translator = translator;
    }

    public Optional<Dimension> findByName(String name, Language language){
        final String NORMALIZED_NAME = StringUtils.stripAccents(name.toUpperCase().trim());
        return Stream.of(Dimension.values())
                .filter(dim -> StringUtils.stripAccents(translator.getLabel(language, dim)).toUpperCase().trim()
                        .equals(NORMALIZED_NAME))
                .findFirst();
    }
}