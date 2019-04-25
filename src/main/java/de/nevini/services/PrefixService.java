package de.nevini.services;

import de.nevini.db.prefix.PrefixData;
import de.nevini.db.prefix.PrefixRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.Guild;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class PrefixService {

    private final PrefixRepository prefixRepository;

    @Getter
    private final String defaultPrefix;
    @Getter
    private final boolean mentionAllowed;
    @Getter
    private final String defaultName;

    public PrefixService(
            @Autowired PrefixRepository prefixRepository,
            @Value("${bot.prefix.default:>}") String defaultPrefix,
            @Value("${bot.prefix.mention:true}") boolean mentionAllowed,
            @Value("${bot.prefix.name:Nevini}") String defaultName
    ) {
        this.prefixRepository = prefixRepository;
        this.defaultPrefix = defaultPrefix;
        this.mentionAllowed = mentionAllowed;
        this.defaultName = defaultName;
    }

    public Collection<String> getGuildPrefixes(Guild guild) {
        return Arrays.asList(
                getGuildPrefix(guild),
                getSelfMention(guild),
                getSelfName(guild)
        );
    }

    public String getGuildPrefix(Guild guild) {
        Optional<PrefixData> data = prefixRepository.findById(guild.getIdLong());
        return data.isPresent() ? data.get().getPrefix() : getDefaultPrefix();
    }

    private String getSelfMention(Guild guild) {
        return guild.getSelfMember().getAsMention();
    }

    private String getSelfName(Guild guild) {
        return '@' + StringUtils.defaultString(guild.getSelfMember().getEffectiveName(), getDefaultName());
    }

    public void setGuildPrefix(Guild guild, String prefix) {
        prefixRepository.save(new PrefixData(guild.getIdLong(), prefix));
    }

}
