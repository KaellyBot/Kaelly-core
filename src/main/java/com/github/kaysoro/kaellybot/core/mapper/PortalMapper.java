package com.github.kaysoro.kaellybot.core.mapper;

import com.github.kaysoro.kaellybot.core.model.constants.Dimension;
import com.github.kaysoro.kaellybot.core.model.constants.Language;
import com.github.kaysoro.kaellybot.core.payloads.portals.PortalDto;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.spec.EmbedCreateSpec;

public final class PortalMapper {

    private PortalMapper(){}

    public static void decorateSpec(EmbedCreateSpec spec, Dimension dimension, PortalDto portal, Language language){
        spec.setTitle(dimension.getLabel(language))
                .setThumbnail(dimension.getImage())
                .setColor(dimension.getColor());

        if (portal.getIsAvailable()){
            spec.addField(Translator.getLabel(language, "pos.position"),
                    "**" + portal.getPosition() + "**", true);

            // Utilisation
            if (portal.getUtilisation() != null)
                spec.addField(Translator.getLabel(language, "pos.utilisation.title"),
                        portal.getUtilisation() + " "
                                + Translator.getLabel(language, "pos.utilisation.desc")
                                + (portal.getUtilisation() > 1 ? "s" : ""), true);

            // Transports
            if (portal.getNearestTransportLimited() != null)
                spec.addField(Translator.getLabel(language, "pos.private_zaap")
                                .replace("{transport}", portal.getNearestTransportLimited().getType()),
                        portal.getNearestTransportLimited().toString(), false);
            spec.addField(Translator.getLabel(language, "pos.zaap"),
                    portal.getNearestZaap().toString(), false);

            // TODO
            // Dates
            // spec.setFooter(getDateInformation(creationSource, updateSource, lg));
        }
        else
            spec.setDescription(Translator.getLabel(language, "pos.unknown"));
    }
}
