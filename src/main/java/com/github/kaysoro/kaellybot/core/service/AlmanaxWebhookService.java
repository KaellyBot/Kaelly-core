package com.github.kaysoro.kaellybot.core.service;

import com.github.kaysoro.kaellybot.core.model.entity.AlmanaxWebhook;
import com.github.kaysoro.kaellybot.core.repository.AlmanaxWebhookRepository;
import discord4j.common.util.Snowflake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AlmanaxWebhookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlmanaxWebhookService.class);

    private final AlmanaxWebhookRepository almanaxWebhookRepository;

    public AlmanaxWebhookService(AlmanaxWebhookRepository almanaxWebhookRepository){
        this.almanaxWebhookRepository = almanaxWebhookRepository;
    }

    public Mono<Boolean> existsById(Snowflake id){
        return almanaxWebhookRepository.existsById(id.asString());
    }

    public Mono<AlmanaxWebhook> save(AlmanaxWebhook almanaxWebhook){
        LOGGER.info("AlmanaxWebhook[id={}] added", almanaxWebhook.getWebhookId());
        return almanaxWebhookRepository.save(almanaxWebhook);
    }

    public Flux<AlmanaxWebhook> findAllByGuildId(Snowflake id){
        return almanaxWebhookRepository.findAllByGuildId(id.asString());
    }

    public Mono<Void> deleteById(Snowflake id){
        LOGGER.info("AlmanaxWebhook[id={}] removed", id);
        return almanaxWebhookRepository.deleteById(id.asString());
    }
}
