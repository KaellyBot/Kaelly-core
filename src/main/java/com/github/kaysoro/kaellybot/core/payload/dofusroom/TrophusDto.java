package com.github.kaysoro.kaellybot.core.payload.dofusroom;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@JsonDeserialize(builder = TrophusDto.TrophusDtoBuilder.class)
@Builder(builderClassName = "TrophusDtoBuilder", toBuilder = true)
public class TrophusDto {

    @JsonProperty("1") ItemDto first;
    @JsonProperty("2") ItemDto second;
    @JsonProperty("3") ItemDto third;
    @JsonProperty("4") ItemDto fourth;
    @JsonProperty("5") ItemDto fifth;
    @JsonProperty("6") ItemDto sixth;

    @JsonPOJOBuilder(withPrefix = "")
    public static class TrophusDtoBuilder {}
}
