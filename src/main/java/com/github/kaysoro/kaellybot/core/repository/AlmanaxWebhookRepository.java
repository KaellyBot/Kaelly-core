package com.github.kaysoro.kaellybot.core.repository;

import com.github.kaysoro.kaellybot.core.model.entity.AlmanaxWebhook;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface AlmanaxWebhookRepository extends ReactiveMongoRepository<AlmanaxWebhook, String> {

    Flux<AlmanaxWebhook> findAllByGuildId(String id);
}
