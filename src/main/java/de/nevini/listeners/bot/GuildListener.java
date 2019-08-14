package de.nevini.listeners.bot;

import de.nevini.jpa.feed.FeedData;
import de.nevini.scope.Feed;
import de.nevini.services.common.FeedService;
import de.nevini.services.dbl.DiscordBotListService;
import de.nevini.util.Formatter;
import de.nevini.util.concurrent.EventDispatcher;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildAvailableEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildUnavailableEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class GuildListener {

    private final DiscordBotListService dblService;
    private final FeedService feedService;

    public GuildListener(
            @Autowired DiscordBotListService dblService,
            @Autowired FeedService feedService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.dblService = dblService;
        this.feedService = feedService;
        eventDispatcher.subscribe(GuildJoinEvent.class, this::onGuildJoin);
        eventDispatcher.subscribe(GuildLeaveEvent.class, this::onGuildLeave);
        eventDispatcher.subscribe(GuildAvailableEvent.class, this::onGuildAvailable);
        eventDispatcher.subscribe(GuildUnavailableEvent.class, this::onGuildUnavailable);
    }

    private void onGuildJoin(GuildJoinEvent event) {
        dblService.updateServerCount();
        Guild guild = event.getGuild();
        Collection<FeedData> subscriptions = feedService.getSubscription(Feed.GUILDS);
        for (FeedData subscription : subscriptions) {
            Guild target = event.getJDA().getGuildById(subscription.getGuild());
            if (target != null) {
                TextChannel channel = target.getTextChannelById(subscription.getChannel());
                if (channel != null) {
                    channel.sendMessage("**" + guild.getSelfMember().getEffectiveName() + "** just joined **"
                            + guild.getName() + "** (" + guild.getId() + ") owned by **"
                            + Optional.ofNullable(guild.getOwner()).map(e -> e.getUser().getAsTag()).orElse("?")
                            + "** (" + guild.getOwnerId() + ") with " + Formatter.formatLong(count(guild, false))
                            + " members and " + Formatter.formatLong(count(guild, true)) + " bots").queue();
                    feedService.updateSubscription(Feed.GUILDS, -1L, channel, System.currentTimeMillis());
                }
            }
        }
    }

    private long count(Guild guild, boolean bots) {
        return guild.getMembers().stream().filter(member -> member.getUser().isBot() == bots).count();
    }

    private void onGuildLeave(GuildLeaveEvent event) {
        dblService.updateServerCount();
        Guild guild = event.getGuild();
        Collection<FeedData> subscriptions = feedService.getSubscription(Feed.GUILDS);
        for (FeedData subscription : subscriptions) {
            Guild target = event.getJDA().getGuildById(subscription.getGuild());
            if (target != null) {
                TextChannel channel = target.getTextChannelById(subscription.getChannel());
                if (channel != null) {
                    channel.sendMessage("**" + guild.getSelfMember().getEffectiveName() + "** just left **"
                            + guild.getName() + "** (" + guild.getId() + ")").queue();
                    feedService.updateSubscription(Feed.GUILDS, -1L, channel, System.currentTimeMillis());
                }
            }
        }
    }

    private void onGuildAvailable(GuildAvailableEvent event) {
        Guild guild = event.getGuild();
        Collection<FeedData> subscriptions = feedService.getSubscription(Feed.GUILDS);
        for (FeedData subscription : subscriptions) {
            Guild target = event.getJDA().getGuildById(subscription.getGuild());
            if (target != null) {
                TextChannel channel = target.getTextChannelById(subscription.getChannel());
                if (channel != null) {
                    channel.sendMessage("**" + guild.getName() + "** (" + guild.getId() + ") became available again")
                            .queue();
                    feedService.updateSubscription(Feed.GUILDS, -1L, channel, System.currentTimeMillis());
                }
            }
        }
    }

    private void onGuildUnavailable(GuildUnavailableEvent event) {
        Guild guild = event.getGuild();
        Collection<FeedData> subscriptions = feedService.getSubscription(Feed.GUILDS);
        for (FeedData subscription : subscriptions) {
            Guild target = event.getJDA().getGuildById(subscription.getGuild());
            if (target != null) {
                TextChannel channel = target.getTextChannelById(subscription.getChannel());
                if (channel != null) {
                    channel.sendMessage("**" + guild.getName() + "** (" + guild.getId() + ") became unavailable")
                            .queue();
                    feedService.updateSubscription(Feed.GUILDS, -1L, channel, System.currentTimeMillis());
                }
            }
        }
    }

}
