package com.github.kaysoro.kaellybot.core.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Graphist {

    ELYCANN("Elycann", "https://www.facebook.com/Elysdrawings/");

    private final String name;
    private final String link;

    public String toMarkdown(){
        return "[" + getName() + "](" + getLink() + ")";
    }
}