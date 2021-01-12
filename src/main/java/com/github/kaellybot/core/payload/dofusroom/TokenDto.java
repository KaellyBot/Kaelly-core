package com.github.kaellybot.core.payload.dofusroom;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = TokenDto.TokenDtoBuilder.class)
@Builder(builderClassName = "TokenDtoBuilder", toBuilder = true)
public class TokenDto {

    String token;

    @JsonPOJOBuilder(withPrefix = "")
    public static class TokenDtoBuilder {
    }
}
