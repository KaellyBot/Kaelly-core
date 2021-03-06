package com.github.kaellybot.core.payload.dofusroom;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = PreviewDto.PreviewDtoBuilder.class)
@Builder(builderClassName = "PreviewDtoBuilder", toBuilder = true)
public class PreviewDto {

    StatusDto status;
    BuildDto data;
    String id;
    String token;
    boolean isPrivate;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PreviewDtoBuilder {}
}
