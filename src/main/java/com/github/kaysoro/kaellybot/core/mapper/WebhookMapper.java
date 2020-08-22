package com.github.kaysoro.kaellybot.core.mapper;

import discord4j.core.object.entity.User;
import discord4j.core.spec.WebhookCreateSpec;
import discord4j.rest.util.Image;
import org.springframework.stereotype.Component;

@Component
public class WebhookMapper {

    public void decorateSpec(WebhookCreateSpec spec, User user, Image avatar){
        spec.setName(user.getUsername())
                .setReason("Almanax")
                .setAvatar(avatar);
    }
}
