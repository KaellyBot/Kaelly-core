package com.github.kaellybot.core.mapper;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.payload.kaelly.portal.PortalDto;
import com.github.kaellybot.core.util.DiscordTranslator;
import discord4j.core.spec.legacy.LegacyEmbedCreateSpec;
import discord4j.rest.util.Color;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class PortalMapper {

    private final DiscordTranslator translator;

    public PortalMapper(DiscordTranslator translator){
        this.translator = translator;
    }

    public void decorateSpec(LegacyEmbedCreateSpec spec, PortalDto portal, Language language){
        spec.setTitle(portal.getDimension().getName())
                .setThumbnail(portal.getDimension().getImage())
                .setColor(Color.of(portal.getDimension().getColor()));

        if (Boolean.TRUE.equals(portal.getIsAvailable())){
            spec.addField(translator.getLabel(language, "pos.position"),
                    "**" + portal.getPosition() + "**", true);

            // Utilisation
            if (portal.getUtilisation() != null)
                spec.addField(translator.getLabel(language, "pos.utilisation.title"),
                        portal.getUtilisation() + " "
                                + translator.getLabel(language, "pos.utilisation.desc")
                                + (portal.getUtilisation() > 1 ? "s" : ""), true);

            // Transports
            if (portal.getNearestTransportLimited() != null)
                spec.addField(translator.getLabel(language, "pos.private_zaap", portal.getNearestTransportLimited().getType()),
                        portal.getNearestTransportLimited().toString(), false);
            spec.addField(translator.getLabel(language, "pos.zaap"),
                    portal.getNearestZaap().toString(), false);

            spec.setFooter(getDateInformation(portal, language), "https://i.imgur.com/u2PUyt5.png");
        }
        else
            spec.setDescription(translator.getLabel(language, "pos.unknown"));
    }

    private String getDateInformation(PortalDto portal, Language language){
        StringBuilder st = new StringBuilder(translator.getLabel(language, "pos.date.added")).append(" ")
                .append(getLabelTimeAgo(portal.getCreationDate(), language)).append(" ")
                .append(translator.getLabel(language, "pos.date.by")).append(" ")
                .append(portal.getCreationAuthor().getName());

        if (portal.getLastUpdateDate() != null){
            st.append(" - ").append(translator.getLabel(language, "pos.date.edited")).append(" ")
                    .append(getLabelTimeAgo(portal.getLastUpdateDate(), language)).append(" ")
                    .append(translator.getLabel(language, "pos.date.by")).append(" ")
                    .append(portal.getLastAuthorUpdate().getName());
        }

        return st.append(" ").append(translator.getLabel(language, "pos.date.via"))
                .append(" dofus-portals.fr").toString();
    }

    private String getLabelTimeAgo(Instant time, Language lg){
        long timeLeft = Math.abs(Duration.between(time, Instant.now()).toMillis());
        if (timeLeft < DateUtils.MILLIS_PER_MINUTE)
            return translator.getLabel(lg, "pos.date.now");
        else if (timeLeft < DateUtils.MILLIS_PER_HOUR)
            return translator.getLabel(lg, "pos.date.minutes_ago", String.valueOf(timeLeft / DateUtils.MILLIS_PER_MINUTE));
        else
            return translator.getLabel(lg, "pos.date.hours_ago", String.valueOf(timeLeft / DateUtils.MILLIS_PER_HOUR));
    }
}
