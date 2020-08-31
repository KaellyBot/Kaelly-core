package com.github.kaellybot.core.payload.dofusroom;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = StuffDto.StuffDtoBuilder.class)
@Builder(builderClassName = "StuffDtoBuilder", toBuilder = true)
public class StuffDto {

    ItemDto amulet;
    @JsonProperty("ring") RingDto rings;
    ItemDto shield;
    ItemDto weapon;
    ItemDto hat;
    ItemDto cape;
    ItemDto belt;
    ItemDto boots;
    ItemDto creature;
    TrophusDto trophus;

    @JsonPOJOBuilder(withPrefix = "")
    public static class StuffDtoBuilder {}
}
