package com.github.kaellybot.core.payload.kaelly.portal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Value
@JsonDeserialize(builder = ExternalPortalDto.ExternalPortalDtoBuilder.class)
@Builder(builderClassName = "ExternalPortalDtoBuilder", toBuilder = true)
public class ExternalPortalDto {
    @NotNull @Valid PositionDto position;
    String author;
    int utilisation;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ExternalPortalDtoBuilder {}
}
