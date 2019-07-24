package de.nevini.services.common;

import de.nevini.jpa.prefix.PrefixData;
import de.nevini.jpa.prefix.PrefixRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
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

    /**
     * Returns the prefix for the provided {@link Guild}.
     * Returns {@link #getDefaultPrefix()} if {@code guild} is {@code null}
     * or the provided {@link Guild} has no prefix in the database.
     *
     * @param guild the relevant {@link Guild} or {@code null} for direct messages
     */
    public @NonNull String getGuildPrefix(Guild guild) {
        if (guild == null) {
            return getDefaultPrefix();
        } else {
            Optional<PrefixData> data = prefixRepository.findById(guild.getIdLong());
            return data.isPresent() ? data.get().getPrefix() : getDefaultPrefix();
        }
    }

    /**
     * Saves a custom bot command prefix for the provided {@link Guild}.
     *
     * @param guild  the relevant {@link Guild}
     * @param prefix the bot command prefix to save
     * @throws NullPointerException     if {@code guild} or {@code prefix} is {@code null}
     * @throws IllegalArgumentException if {@code prefix} is longer than 32 characters
     */
    public void setGuildPrefix(@NonNull Guild guild, @NonNull String prefix) {
        if (prefix.length() > 32) {
            throw new IllegalArgumentException("prefix is longer than 32 characters!");
        }
        PrefixData data = new PrefixData(guild.getIdLong(), prefix);
        log.info("Save data: {}", data);
        prefixRepository.save(data);
    }

    /**
     * Attempts to extract the bot command prefix from the provided {@link MessageReceivedEvent}.
     * Note that an empty {@link String} can be a valid prefix, e.g. for direct messages.
     * Returns {@code null} if no matching prefix was found.
     *
     * @param event the {@link MessageReceivedEvent}
     * @throws NullPointerException if {@code event} is {@code null}
     */
    public @NonNull String extractPrefix(@NonNull MessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw();

        // check for the guild prefix (assumed to be the most commonly used one)
        String guildPrefix = getGuildPrefix(event.getGuild());
        if (content.startsWith(guildPrefix)) {
            return guildPrefix;
        }

        // check for member mention
        if (event.getGuild() != null) {
            Member selfMember = event.getGuild().getSelfMember();
            if (content.startsWith(selfMember.getAsMention())) {
                return selfMember.getAsMention();
            } else if (content.matches("\\Q@" + selfMember.getEffectiveName() + "\\E\\b.*")) {
                return '@' + selfMember.getEffectiveName();
            } else if (content.matches("\\Q@!" + selfMember.getEffectiveName() + "\\E\\b.*")) {
                return "@!" + selfMember.getEffectiveName();
            }
        }

        // check for user mention
        User selfUser = event.getJDA().getSelfUser();
        if (content.startsWith(selfUser.getAsMention())) {
            return selfUser.getAsMention();
        } else if (content.matches("\\Q@" + selfUser.getName() + "\\E\\b.*")) {
            return '@' + selfUser.getName();
        } else if (content.matches("\\Q@" + selfUser.getAsTag() + "\\E\\b.*")) {
            return '@' + selfUser.getAsTag();
        } else if (content.matches("\\Q@" + selfUser.getId() + "\\E\\b.*")) {
            return '@' + selfUser.getId();
        }

        // check for direct message
        if (event.isFromType(ChannelType.PRIVATE)) {
            return StringUtils.EMPTY;
        }

        // no match found
        return null;
    }

}