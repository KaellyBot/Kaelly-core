package com.github.kaellybot.core.payload.kaelly.portal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = DimensionDto.DimensionDtoBuilder.class)
@Builder(builderClassName = "DimensionDtoBuilder", toBuilder = true)
public class DimensionDto {

    String name;
    String image;
    int color;

    @JsonPOJOBuilder(withPrefix = "")
    public static class DimensionDtoBuilder {}
}