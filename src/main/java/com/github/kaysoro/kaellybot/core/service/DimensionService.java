package com.github.kaysoro.kaellybot.core.service;

import com.github.kaysoro.kaellybot.core.model.constant.Dimension;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class DimensionService {

    public Optional<Dimension> findByName(String name){
        final String NORMALIZED_NAME = StringUtils.stripAccents(name.toUpperCase().trim());
        return Stream.of(Dimension.values())
                .filter(dim -> StringUtils.stripAccents(dim.name().toUpperCase().trim())
                        .equals(NORMALIZED_NAME))
                .findFirst();
    }
}