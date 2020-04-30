package com.github.kaysoro.kaellybot.core.payload.dofusroom;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = RingDto.RingDtoBuilder.class)
@Builder(builderClassName = "RingDtoBuilder", toBuilder = true)
public class RingDto {

    ItemDto top;
    ItemDto bottom;

    @JsonPOJOBuilder(withPrefix = "")
    public static class RingDtoBuilder {}
}
