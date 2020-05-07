package com.github.kaysoro.kaellybot.core.payload.kaelly.portal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@JsonDeserialize(builder = PortalDto.PortalDtoBuilder.class)
@Builder(builderClassName = "PortalDtoBuilder", toBuilder = true)
public class PortalDto {
    String dimension;
    PositionDto position;
    Boolean isAvailable;
    Integer utilisation;
    Instant creationDate;
    AuthorDto creationAuthor;
    Instant lastUpdateDate;
    AuthorDto lastAuthorUpdate;
    TransportDto nearestZaap;
    TransportDto nearestTransportLimited;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PortalDtoBuilder {}
}