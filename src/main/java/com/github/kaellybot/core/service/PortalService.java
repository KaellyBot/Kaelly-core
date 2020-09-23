package com.github.kaellybot.core.service;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.model.entity.Dimension;
import com.github.kaellybot.commons.model.entity.Server;
import com.github.kaellybot.core.payload.kaelly.portal.PortalDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;

@Service
public class PortalService extends AbstractRestClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PortalService.class);
    private static final String API_BASE_URL = "/api";

    private final WebClient webClient;

    public PortalService(@Value("${kaelly.portals.url}") String portalUrl,
                         @Value("${kaelly.portals.username}") String portalUsername,
                         @Value("${kaelly.portals.password}") String portalPassword) {
        this.webClient = WebClient.builder()
                .baseUrl(portalUrl + API_BASE_URL)
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .filter(logRequest(LOGGER))
                .filter(ExchangeFilterFunctions
                        .basicAuthentication(portalUsername, portalPassword))
                .build();
    }

    public Mono<PortalDto> getPortal(Server server, Dimension dimension, Language language){
        return webClient.get()
                .uri("/{server}/portals?dimension={dimension}",
                        server.getLabels().get(language), dimension.getLabels().get(language))
                .header(ACCEPT_LANGUAGE, language.getAbbreviation())
                .retrieve()
                .bodyToMono(PortalDto.class);
    }

    public Flux<PortalDto> getPortals(Server server, Language language){
        return webClient.get()
                .uri("/{server}/portals", server.getLabels().get(language))
                .header(ACCEPT_LANGUAGE, language.getAbbreviation())
                .retrieve()
                .bodyToFlux(PortalDto.class);
    }
}