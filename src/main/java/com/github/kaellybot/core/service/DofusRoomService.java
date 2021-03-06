package com.github.kaellybot.core.service;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.payload.dofusroom.PreviewDto;
import com.github.kaellybot.core.payload.dofusroom.TokenDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;

@Service
public class DofusRoomService extends AbstractRestClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DofusRoomService.class);
    private static final String ORGANIZATION = "organization";
    private static final String API_KEY = "apiKey";

    private final WebClient webClient;

    public DofusRoomService(@Value("${dofusroom.url}") String dofusRoomUrl,
                            @Value("${dofusroom.token}") String dofusRoomToken,
                            @Value("${dofusroom.organization}") String dofusRoomOrganization) {
        this.webClient = WebClient.builder()
                .baseUrl(dofusRoomUrl)
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .defaultHeader(ORGANIZATION, dofusRoomOrganization)
                .defaultHeader(API_KEY, dofusRoomToken)
                .filter(logRequest(LOGGER))
                .build();
    }

    public Mono<PreviewDto> getDofusRoomPreview(String id, String token, Language language){
        return webClient.post()
                .uri("/{id}", id)
                .header(ACCEPT_LANGUAGE, language.getAbbreviation())
                .bodyValue(TokenDto.builder().token(token).build())
                .retrieve()
                .bodyToMono(PreviewDto.class)
                .map(preview -> preview.toBuilder().id(id).token(token).isPrivate(token != null && ! token.trim().isEmpty()).build());
    }
}