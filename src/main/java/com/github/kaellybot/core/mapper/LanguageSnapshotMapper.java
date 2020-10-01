package com.github.kaellybot.core.mapper;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.model.constant.Constants;
import com.github.kaellybot.core.model.entity.Guild;
import com.github.kaellybot.core.util.DiscordTranslator;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class LanguageSnapshotMapper {

    private final DiscordTranslator translator;

    public void decorateSpec(EmbedCreateSpec spec, discord4j.core.object.entity.Guild discordGuild,
                             List<GuildChannel> channels, Guild guild, Language language){
        spec.setTitle(translator.getLabel(language, "lang.status_title"))
                .setDescription(translator.getLabel(language, "lang.known_language", language))
                .setColor(Color.of(Constants.COLOR))
                .setThumbnail(language.getImage())
                .addField(":book:", getUsedLanguageListing(discordGuild, guild.getLanguage(), channels,
                        guild.getChannelLanguageList(), language), true)
                .addField(translator.getLabel(language, "lang.available_languages"),
                        getAvailableLanguages(), false)
                .setFooter(Constants.NAME, Constants.AVATAR);
    }

    private String getUsedLanguageListing(discord4j.core.object.entity.Guild guild, Language guildLanguage,
                                          List<GuildChannel> channels, List<Guild.ChannelLanguage>  channelLanguages,
                                          Language language){
        Map<String, GuildChannel> channelsMap = channels.parallelStream()
                .collect(Collectors.toMap(channel -> channel.getId().asString(), Function.identity()));
        StringBuilder st = new StringBuilder(translator.getLabel(language, "lang.listing",
                guild.getName(), guildLanguage.getAbbreviation())).append("\n");

        for(Guild.ChannelLanguage channelLanguage : channelLanguages)
            if (channelsMap.containsKey(channelLanguage.getId()))
                st.append(translator.getLabel(language, "lang.listing", channelsMap
                        .get(channelLanguage.getId()).getMention(), channelLanguage.getLanguage().getAbbreviation()))
                        .append("\n");

        return st.toString();
    }

    private String getAvailableLanguages(){
        return translator.getRegisteredLanguages().stream()
                .filter(Language::isDisplayed)
                .sorted(Comparator.comparing(Language::getAbbreviation))
                .map(language -> language.getEmoji() + " (" + language.getAbbreviation() + ")")
                .collect(Collectors.joining(", "));
    }
}