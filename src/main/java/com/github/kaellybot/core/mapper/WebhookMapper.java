package com.github.kaellybot.core.mapper;

import discord4j.core.object.entity.User;
import discord4j.core.spec.WebhookCreateSpec;
import discord4j.core.spec.legacy.LegacyWebhookCreateSpec;
import discord4j.rest.util.Image;
import org.springframework.stereotype.Component;

@Component
public class WebhookMapper {

    public void decorateSpec(LegacyWebhookCreateSpec spec, User user, Image avatar){
        spec.setName(user.getUsername())
                .setReason("Almanax")
                .setAvatar(avatar);
    }
}
