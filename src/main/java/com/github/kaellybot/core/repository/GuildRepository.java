package com.github.kaellybot.core.repository;

import com.github.kaellybot.core.model.entity.Guild;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface GuildRepository extends ReactiveMongoRepository<Guild, String> {
}
