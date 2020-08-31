package com.github.kaellybot.core.payload.dofusroom;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = ItemDto.ItemDtoBuilder.class)
@Builder(builderClassName = "ItemDtoBuilder", toBuilder = true)
public class ItemDto {

    String name;
    @JsonProperty("url") String id;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ItemDtoBuilder {}
}
