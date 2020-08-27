package de.nevini.modules.guild.automod;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.core.scope.Node;
import de.nevini.modules.core.module.services.ModuleService;
import de.nevini.modules.core.permission.services.PermissionService;
import de.nevini.util.concurrent.EventDispatcher;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.audit.AuditLogOption;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
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
        Message message;
        List<IMentionable> mentions;
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
                    receivedCache.put(event.getMessageIdLong(), new CachedMessage(event.getMessage(), mentions));
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
                postWarning(event.getChannel(), event.getMember(), message.getMessage());
            }
            // update cache if new mentions were added
            if (!message.getMentions().containsAll(newMentions)) {
                log.debug("Caching mentions for message {} (updated): {}", event.getMessageIdLong(), newMentions);
                receivedCache.put(event.getMessageIdLong(), new CachedMessage(message.getMessage(), newMentions));
            }
        }
    }

    private void onMessageDelete(GuildMessageDeleteEvent event) {
        CachedMessage message = receivedCache.getIfPresent(event.getMessageIdLong());
        if (message != null) {
            // check audit log
            if (event.getGuild().getSelfMember().hasPermission(Permission.VIEW_AUDIT_LOGS)) {
                event.getGuild().retrieveAuditLogs().type(ActionType.MESSAGE_DELETE).limit(1).queue(
                        auditLog -> acceptMessageDeleteAuditLog(event, message, auditLog),
                        ignore -> acceptMessageDeleteWithoutAuditLog(event, message)
                );
            } else {
                acceptMessageDeleteWithoutAuditLog(event, message);
            }
        }
    }

    private void acceptMessageDeleteAuditLog(
            GuildMessageDeleteEvent event, CachedMessage message, List<AuditLogEntry> auditLog
    ) {
        // find the culprit in the audit log
        Member culprit = auditLog.stream().filter(e ->
                // entry id must be more recent than that of the deleted message
                e.getIdLong() > message.getMessage().getIdLong()
                        // entry target must match the author of the original message
                        && e.getTargetIdLong() == message.getMessage().getAuthor().getIdLong()
                        // entry options must match the channel of the original message
                        && message.getMessage().getChannel().getId().equals(e.getOption(AuditLogOption.CHANNEL))
        ).findFirst().map(AuditLogEntry::getUser).map(user -> event.getGuild().getMember(user))
                // if the audit log contains no entry, the author of the message most likely deleted their own message
                .orElse(message.getMessage().getMember());
        // check immunity
        if (culprit == null || !permissionService.hasChannelUserPermission(event.getChannel(), culprit, IMMUNITY)) {
            // post warning
            log.debug("Posting ghost ping warning for message {} (deleted)", event.getMessageIdLong());
            postWarning(event.getChannel(), culprit, message.getMessage());
        }
    }

    private void acceptMessageDeleteWithoutAuditLog(GuildMessageDeleteEvent event, CachedMessage message) {
        // post warning
        log.debug("Posting ghost ping warning for message {} (deleted)", event.getMessageIdLong());
        postWarning(event.getChannel(), null, message.getMessage());
    }

    private void postWarning(TextChannel channel, Member member, Message message) {
        Member author = message.getMember();
        Member selfMember = channel.getGuild().getSelfMember();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (author == null) {
            embedBuilder.setAuthor(channel.getGuild().getName(), null, channel.getGuild().getIconUrl());
            embedBuilder.setColor(selfMember.getColor());
        } else {
            embedBuilder.setAuthor(author.getEffectiveName(), null, author.getUser().getAvatarUrl());
            embedBuilder.setColor(author.getColor());
        }
        embedBuilder.setDescription(message.getContentRaw());
        embedBuilder.setFooter("Original message");
        embedBuilder.setTimestamp(message.getTimeCreated());

        MessageBuilder messageBuilder = new MessageBuilder();
        if (member == null) {
            messageBuilder.setContent("Please do not ghost ping.");
        } else {
            messageBuilder.setContent(member.getAsMention() + " Please do not ghost ping.");
        }
        messageBuilder.setEmbed(embedBuilder.build());
        channel.sendMessage(messageBuilder.build()).queue();
    }

}
