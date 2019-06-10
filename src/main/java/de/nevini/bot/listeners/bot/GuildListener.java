package de.nevini.bot.listeners.bot;

import de.nevini.bot.db.feed.FeedData;
import de.nevini.bot.scope.Feed;
import de.nevini.bot.services.common.FeedService;
import de.nevini.commons.concurrent.EventDispatcher;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.GuildAvailableEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.GuildUnavailableEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class GuildListener {

    private final FeedService feedService;

    public GuildListener(
            @Autowired FeedService feedService,
            @Autowired EventDispatcher<Event> eventDispatcher
    ) {
        this.feedService = feedService;
        eventDispatcher.subscribe(GuildJoinEvent.class, this::onGuildJoin);
        eventDispatcher.subscribe(GuildLeaveEvent.class, this::onGuildLeave);
        eventDispatcher.subscribe(GuildAvailableEvent.class, this::onGuildAvailable);
        eventDispatcher.subscribe(GuildUnavailableEvent.class, this::onGuildUnavailable);
    }

    private void onGuildJoin(GuildJoinEvent event) {
        Guild guild = event.getGuild();
        Collection<FeedData> subscriptions = feedService.getSubscription(Feed.GUILDS);
        for (FeedData subscription : subscriptions) {
            Guild target = event.getJDA().getGuildById(subscription.getGuild());
            if (target != null) {
                TextChannel channel = target.getTextChannelById(subscription.getChannel());
                if (channel != null) {
                    channel.sendMessage("**" + guild.getSelfMember().getEffectiveName() + "** just joined **"
                            + guild.getName() + "** (" + guild.getId() + ") owned by **"
                            + guild.getOwner().getUser().getAsTag() + "** (" + guild.getOwnerId() + ") with "
                            + guild.getMembers().size() + " members").queue();
                }
                feedService.updateSubscription(Feed.GUILDS, -1L, channel, System.currentTimeMillis());
            }
        }

    }


    private void onGuildLeave(GuildLeaveEvent event) {
        Guild guild = event.getGuild();
        Collection<FeedData> subscriptions = feedService.getSubscription(Feed.GUILDS);
        for (FeedData subscription : subscriptions) {
            Guild target = event.getJDA().getGuildById(subscription.getGuild());
            if (target != null) {
                TextChannel channel = target.getTextChannelById(subscription.getChannel());
                if (channel != null) {
                    channel.sendMessage("**" + guild.getSelfMember().getEffectiveName() + "** just left **"
                            + guild.getName() + "** (" + guild.getId() + ")").queue();
                }
                feedService.updateSubscription(Feed.GUILDS, -1L, channel, System.currentTimeMillis());
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
                }
                feedService.updateSubscription(Feed.GUILDS, -1L, channel, System.currentTimeMillis());
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
                }
                feedService.updateSubscription(Feed.GUILDS, -1L, channel, System.currentTimeMillis());
            }
        }
    }

}
