package com.github.kaellybot.core.payload.dofusroom;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StatusDto {
    @JsonProperty("success") SUCCESS,
    @JsonProperty("error") ERROR
}
