package com.github.kaysoro.kaellybot.core.payload.kaelly.portal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = TransportDto.TransportDtoBuilder.class)
@Builder(builderClassName = "TransportDtoBuilder", toBuilder = true)
public class TransportDto {
    String type;
    String area;
    String subArea;
    PositionDto position;
    boolean isAvailableUnderConditions;

    @JsonPOJOBuilder(withPrefix = "")
    public static class TransportDtoBuilder {}

    @Override
    public String toString(){
        return area + " (" + subArea + ") **" + position.toString() + "**";
    }
}