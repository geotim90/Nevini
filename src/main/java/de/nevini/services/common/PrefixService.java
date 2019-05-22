package de.nevini.services.common;

import de.nevini.db.prefix.PrefixData;
import de.nevini.db.prefix.PrefixRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class PrefixService {

    private final PrefixRepository prefixRepository;

    @Getter
    private final String defaultPrefix;

    public PrefixService(
            @Autowired PrefixRepository prefixRepository,
            @Value("${bot.prefix.default:>}") String defaultPrefix
    ) {
        this.prefixRepository = prefixRepository;
        this.defaultPrefix = defaultPrefix;
    }

    public String getGuildPrefix(Guild guild) {
        Optional<PrefixData> data = guild != null ? prefixRepository.findById(guild.getIdLong()) : Optional.empty();
        return data.isPresent() ? data.get().getPrefix() : getDefaultPrefix();
    }

    public synchronized void setGuildPrefix(@NonNull Guild guild, String prefix) {
        PrefixData data = new PrefixData(guild.getIdLong(), prefix);
        log.info("Save data: {}", data);
        prefixRepository.save(data);
    }

    public Optional<String> extractPrefix(MessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw();
        String configuredPrefix = getGuildPrefix(event.getGuild());
        if (content.startsWith(configuredPrefix)) {
            return Optional.of(configuredPrefix);
        } else if (content.startsWith("<@" + event.getJDA().getSelfUser().getId() + ">")) {
            return Optional.of("<@" + event.getJDA().getSelfUser().getId() + ">");
        } else if (content.startsWith("<@!" + event.getJDA().getSelfUser().getId() + ">")) {
            return Optional.of("<@!" + event.getJDA().getSelfUser().getId() + ">");
        } else if (content.startsWith("@" + event.getJDA().getSelfUser().getName())) {
            return Optional.of("@" + event.getJDA().getSelfUser().getName());
        } else if (event.isFromType(ChannelType.TEXT)
                && content.startsWith("@" + event.getGuild().getSelfMember().getEffectiveName())) {
            return Optional.of("@" + event.getGuild().getSelfMember().getEffectiveName());
        } else {
            return event.isFromType(ChannelType.TEXT) ? Optional.empty() : Optional.of(StringUtils.EMPTY);
        }
    }

}
