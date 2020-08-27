package de.nevini.modules.guild.tribute.listeners;

import de.nevini.core.scope.Feed;
import de.nevini.modules.guild.feed.data.FeedData;
import de.nevini.modules.guild.feed.services.FeedService;
import de.nevini.modules.guild.tribute.services.TributeService;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandReaction;
import de.nevini.util.concurrent.EventDispatcher;
import lombok.Data;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Component
public class TributeRoleListener {

    private final TributeService tributeService;
    private final FeedService feedService;

    private long lastUpdate = 0L;

    public TributeRoleListener(
            @Autowired TributeService tributeService,
            @Autowired FeedService feedService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.tributeService = tributeService;
        this.feedService = feedService;
        eventDispatcher.subscribe(GuildMemberRoleAddEvent.class, this::onMemberRoleAdd);
        eventDispatcher.subscribe(UserUpdateOnlineStatusEvent.class, this::onUpdateOnlineStatus);
    }

    private void onMemberRoleAdd(GuildMemberRoleAddEvent e) {
        Role role = tributeService.getRole(e.getGuild());
        if (role != null && e.getRoles().contains(role)) {
            OffsetDateTime now = OffsetDateTime.now();
            Long start = tributeService.getStart(e.getMember());
            if (start == null) {
                tributeService.setStartIfNull(e.getMember(), System.currentTimeMillis());
            } else {
                long delay = ChronoUnit.DAYS.between(
                        OffsetDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneOffset.UTC), now);
                tributeService.setDelay(e.getMember(), delay);
            }
        }
    }

    private synchronized void onUpdateOnlineStatus(UserUpdateOnlineStatusEvent e) {
        // only execute once every minute at maximum
        long now = System.currentTimeMillis();
        if (now - lastUpdate < TimeUnit.MINUTES.toMillis(1)) return;

        // process all active subscriptions
        Collection<FeedData> subscriptions = feedService.getSubscription(Feed.TRIBUTE_TIMEOUTS);
        for (FeedData subscription : subscriptions) {
            Guild guild = e.getJDA().getGuildById(subscription.getGuild());
            if (guild == null) continue;
            TextChannel channel = guild.getTextChannelById(subscription.getChannel());
            if (channel == null) continue;
            long uts = ObjectUtils.defaultIfNull(subscription.getUts(), 0L);
            Role role = tributeService.getRole(guild);
            if (role == null) continue;
            Long contributionTimeoutInDays = tributeService.getTimeout(guild);
            if (contributionTimeoutInDays == null) continue;
            StringBuilder builder = new StringBuilder();
            guild.getMembers().stream().filter(
                    member -> !member.getUser().isBot() && member.getRoles().contains(role)
            ).map(member -> {
                MemberReportDetails memberDetails = new MemberReportDetails();
                memberDetails.setMember(member);
                memberDetails.setJoined(tributeService.getStart(member));
                memberDetails.setContribution(tributeService.getTribute(member));
                long contributionDelayInDays = ObjectUtils.defaultIfNull(tributeService.getDelay(member), 0L);
                if (memberDetails.getJoined() != null) {
                    memberDetails.setDeadline(OffsetDateTime.ofInstant(Instant.ofEpochMilli(memberDetails.getJoined()),
                            ZoneOffset.UTC).plusDays(contributionTimeoutInDays + contributionDelayInDays).toInstant()
                            .toEpochMilli());
                }
                return memberDetails;
            }).filter(member -> member.getDeadline() > uts && member.getDeadline() <= now).forEach(member -> {
                if (member.isContribution()) {
                    builder.append(CommandReaction.OK.getUnicode()).append(" **")
                            .append(member.getMember().getEffectiveName())
                            .append("** has contributed - the deadline was ")
                            .append(Formatter.formatTimestamp(member.getDeadline())).append("\n");
                } else {
                    builder.append(CommandReaction.ERROR.getUnicode()).append(" **")
                            .append(member.getMember().getEffectiveName())
                            .append("** failed to contribute - the deadline was ")
                            .append(Formatter.formatTimestamp(member.getDeadline())).append("\n");
                }
            });
            String message = builder.toString();
            if (!message.isEmpty()) {
                channel.sendMessage(builder.toString()).queue();
            }
            feedService.updateSubscription(Feed.TRIBUTE_TIMEOUTS, -1L, channel, now);
        }
        lastUpdate = now;
    }

    @Data
    private static class MemberReportDetails {
        Member member;
        @Nullable
        Long joined;
        boolean contribution;
        long deadline;
    }

}
