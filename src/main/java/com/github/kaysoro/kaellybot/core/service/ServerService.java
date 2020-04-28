package com.github.kaysoro.kaellybot.core.service;

import com.github.kaysoro.kaellybot.core.model.constant.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ServerService {

    public Optional<Server> findByName(String name) {
        final String NORMALIZED_NAME = StringUtils.stripAccents(name.toUpperCase().trim());
        return Stream.of(Server.values())
                .filter(server -> StringUtils.stripAccents(server.name().toUpperCase().trim())
                        .equals(NORMALIZED_NAME))
                .findFirst();
    }
}