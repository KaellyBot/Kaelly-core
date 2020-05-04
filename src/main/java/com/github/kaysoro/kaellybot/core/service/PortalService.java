package com.github.kaysoro.kaellybot.core.service;

import com.github.kaysoro.kaellybot.core.model.constant.Dimension;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.model.constant.Server;
import com.github.kaysoro.kaellybot.core.payload.kaelly.portal.PortalDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;

@Service
public class PortalService extends AbstractRestClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PortalService.class);
    private static final String API_BASE_URL = "/api";

    @Value("${kaelly.portals.token}")
    private String portalToken;

    private final WebClient webClient;

    public PortalService(@Value("${kaelly.portals.url}") String portalUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(portalUrl + API_BASE_URL)
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .filter(logRequest(LOGGER))
                .build();
    }

    public Mono<PortalDto> getPortal(Server server, Dimension dimension, Language language){
        return webClient.get()
                .uri("/{server}/portals?dimension={dimension}&token={token}",
                        server.name(), dimension.name(), portalToken)
                .header(ACCEPT_LANGUAGE, language.name())
                .retrieve()
                .bodyToMono(PortalDto.class);
    }

    public Flux<PortalDto> getPortals(Server server, Language language){
        return webClient.get()
                .uri("/{server}/portals?token={token}", server.name(), portalToken)
                .header(ACCEPT_LANGUAGE, language.name())
                .retrieve()
                .bodyToFlux(PortalDto.class);
    }
}