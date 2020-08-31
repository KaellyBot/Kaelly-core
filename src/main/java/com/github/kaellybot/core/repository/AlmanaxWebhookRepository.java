package com.github.kaellybot.core.repository;

import com.github.kaellybot.core.model.entity.AlmanaxWebhook;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface AlmanaxWebhookRepository extends ReactiveMongoRepository<AlmanaxWebhook, String> {

    Flux<AlmanaxWebhook> findAllByGuildId(String id);
}
