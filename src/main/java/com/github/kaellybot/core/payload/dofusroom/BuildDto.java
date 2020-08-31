package com.github.kaellybot.core.payload.dofusroom;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = BuildDto.BuildDtoBuilder.class)
@Builder(builderClassName = "BuildDtoBuilder", toBuilder = true)
public class BuildDto {

    String message;
    String name;
    String author;
    String character;
    Integer level;
    StuffDto items;

    @JsonPOJOBuilder(withPrefix = "")
    public static class BuildDtoBuilder {}
}
