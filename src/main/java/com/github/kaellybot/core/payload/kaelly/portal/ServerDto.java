package com.github.kaellybot.core.payload.kaelly.portal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = ServerDto.ServerDtoBuilder.class)
@Builder(builderClassName = "ServerDtoBuilder", toBuilder = true)
public class ServerDto {

    String name;
    String image;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ServerDtoBuilder {}
}