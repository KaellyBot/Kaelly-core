package com.github.kaysoro.kaellybot.core.mapper;

import com.github.kaysoro.kaellybot.core.model.constants.Dimension;
import com.github.kaysoro.kaellybot.core.model.constants.Language;
import com.github.kaysoro.kaellybot.core.payloads.portals.PortalDto;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.spec.EmbedCreateSpec;
import org.apache.commons.lang3.time.DateUtils;

import java.time.Duration;
import java.time.Instant;

public final class PortalMapper {

    private PortalMapper(){}

    public static void decorateSpec(EmbedCreateSpec spec, PortalDto portal, Language language){
        Dimension dimension = Dimension.valueOf(portal.getDimension(), language);

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

            spec.setFooter(getDateInformation(portal, language),
                    "http://image.noelshack.com/fichiers/2019/43/1/1571689446-zaap2.png");
        }
        else
            spec.setDescription(Translator.getLabel(language, "pos.unknown"));
    }

    private static String getDateInformation(PortalDto portal, Language language){
        StringBuilder st = new StringBuilder(Translator.getLabel(language, "pos.date.added")).append(" ")
                .append(getLabelTimeAgo(portal.getCreationDate(), language)).append(" ")
                .append(Translator.getLabel(language, "pos.date.by")).append(" ")
                .append(portal.getCreationAuthor().getName());

        if (portal.getLastUpdateDate() != null){
            st.append(" - ").append(Translator.getLabel(language, "pos.date.edited")).append(" ")
                    .append(getLabelTimeAgo(portal.getLastUpdateDate(), language)).append(" ")
                    .append(Translator.getLabel(language, "pos.date.by")).append(" ")
                    .append(portal.getLastAuthorUpdate().getName());
        }

        return st.append(" ").append(Translator.getLabel(language, "pos.date.via"))
                .append(" dofus-portals.fr").toString();
    }

    private static String getLabelTimeAgo(Instant time, Language lg){
        long timeLeft = Math.abs(Duration.between(time, Instant.now()).toMillis());
        if (timeLeft < DateUtils.MILLIS_PER_MINUTE)
            return Translator.getLabel(lg, "pos.date.now");
        else if (timeLeft < DateUtils.MILLIS_PER_HOUR)
            return Translator.getLabel(lg, "pos.date.minutes_ago")
                    .replace("{time}", String.valueOf(timeLeft / DateUtils.MILLIS_PER_MINUTE));
        else
            return Translator.getLabel(lg, "pos.date.hours_ago")
                    .replace("{time}", String.valueOf(timeLeft / DateUtils.MILLIS_PER_HOUR));
    }
}
