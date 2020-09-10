package de.nevini.modules.admin.guilds.listeners;

import de.nevini.core.scope.Feed;
import de.nevini.core.services.DiscordBotListService;
import de.nevini.modules.guild.feed.data.FeedData;
import de.nevini.modules.guild.feed.services.FeedService;
import de.nevini.util.Formatter;
import de.nevini.util.concurrent.EventDispatcher;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
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
        eventDispatcher.subscribe(ReadyEvent.class, ignore -> onReady());
        eventDispatcher.subscribe(GuildJoinEvent.class, this::onGuildJoin);
        eventDispatcher.subscribe(GuildLeaveEvent.class, this::onGuildLeave);
    }

    private void onReady() {
        dblService.updateServerCount();
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
                            + "** (" + guild.getOwnerId() + ") with " + Formatter.formatInteger(count(guild, false))
                            + " members and " + Formatter.formatInteger(count(guild, true)) + " bots").queue();
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

}
