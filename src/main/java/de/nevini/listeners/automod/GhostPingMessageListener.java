package de.nevini.listeners.automod;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.scope.Node;
import de.nevini.services.common.ModuleService;
import de.nevini.services.common.PermissionService;
import de.nevini.util.concurrent.EventDispatcher;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@Component
public class GhostPingMessageListener {

    private static final Node IMMUNITY = Node.GUILD_AUTO_MOD_GHOST_PING_IMMUNITY;
    private static final MentionType[] MENTIONS = {
            MentionType.EVERYONE, MentionType.HERE, MentionType.ROLE, MentionType.USER
    };

    private final ModuleService moduleService;
    private final PermissionService permissionService;

    /**
     * Tracks mentions of messages received in the last minute.
     */
    private final Cache<Long, CachedMessage> receivedCache;

    @Value
    private static class CachedMessage {
        private final String content;
        private final List<IMentionable> mentions;
    }

    public GhostPingMessageListener(
            @Autowired ModuleService moduleService,
            @Autowired PermissionService permissionService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.moduleService = moduleService;
        this.permissionService = permissionService;
        receivedCache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).build();
        eventDispatcher.subscribe(GuildMessageReceivedEvent.class, this::onMessageReceived);
        eventDispatcher.subscribe(GuildMessageUpdateEvent.class, this::onMessageUpdate);
        eventDispatcher.subscribe(GuildMessageDeleteEvent.class, this::onMessageDelete);
    }

    private void onMessageReceived(GuildMessageReceivedEvent event) {
        // get member
        Member member = event.getMember();
        if (member != null && !member.getUser().isBot()) {
            // check for mentions
            List<IMentionable> mentions = event.getMessage().getMentions(MENTIONS);
            if (!mentions.isEmpty()) {
                // check immunity
                if (moduleService.isModuleActive(event.getGuild(), IMMUNITY.getModule())
                        && !permissionService.hasChannelUserPermission(event.getChannel(), member, IMMUNITY)
                ) {
                    // cache mentions for one minute
                    log.debug("Caching mentions for message {}: {}", event.getMessageIdLong(), mentions);
                    receivedCache.put(event.getMessageIdLong(),
                            new CachedMessage(event.getMessage().getContentRaw(), mentions));
                }
            }
        }
    }

    private void onMessageUpdate(GuildMessageUpdateEvent event) {
        CachedMessage message = receivedCache.getIfPresent(event.getMessageIdLong());
        if (message != null) {
            List<IMentionable> newMentions = event.getMessage().getMentions(MENTIONS);
            // warn author if old mentions were removed
            if (!newMentions.containsAll(message.getMentions())) {
                log.debug("Posting ghost ping warning for message {} (updated)", event.getMessageIdLong());
                postWarning(event.getChannel(), event.getMember(), message.getContent());
            }
            // update cache if new mentions were added
            if (!message.getMentions().containsAll(newMentions)) {
                log.debug("Caching mentions for message {} (updated): {}", event.getMessageIdLong(), newMentions);
                receivedCache.put(event.getMessageIdLong(), new CachedMessage(message.getContent(), newMentions));
            }
        }
    }

    private void onMessageDelete(GuildMessageDeleteEvent event) {
        CachedMessage message = receivedCache.getIfPresent(event.getMessageIdLong());
        if (message != null) {
            log.debug("Posting ghost ping warning for message {} (deleted)", event.getMessageIdLong());
            postWarning(event.getChannel(), null, message.getContent());
        }
    }

    private void postWarning(TextChannel channel, Member member, String content) {
        Member selfMember = channel.getGuild().getSelfMember();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (member == null) {
            embedBuilder.setAuthor(channel.getGuild().getName(), null, channel.getGuild().getIconUrl());
            embedBuilder.setColor(selfMember.getColor());
        } else {
            embedBuilder.setAuthor(member.getEffectiveName(), null, member.getUser().getAvatarUrl());
            embedBuilder.setColor(member.getColor());
        }
        embedBuilder.setDescription(content);
        embedBuilder.setFooter(selfMember.getEffectiveName(), selfMember.getUser().getAvatarUrl());
        embedBuilder.setTimestamp(Instant.now());
        embedBuilder.setTitle("Original message:");

        MessageBuilder messageBuilder = new MessageBuilder();
        if (member != null) {
            messageBuilder.setContent(member.getAsMention() + " Please do not ghost ping.");
        } else {
            messageBuilder.setContent("Please do not ghost ping.");
        }
        messageBuilder.setEmbed(embedBuilder.build());
        messageBuilder.sendTo(channel).queue();
    }

}
