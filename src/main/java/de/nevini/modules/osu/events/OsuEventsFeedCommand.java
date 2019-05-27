package de.nevini.modules.osu.events;

import de.nevini.command.*;
import de.nevini.resolvers.common.ChannelResolver;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Feed;
import de.nevini.scope.Node;
import net.dv8tion.jda.core.entities.TextChannel;

public class OsuEventsFeedCommand extends Command {

    public OsuEventsFeedCommand() {
        super(CommandDescriptor.builder()
                .keyword("feed")
                .aliases(new String[]{"subscribe"})
                .node(Node.FEED_OSU_STATS)
                .description("subscribes to osu! events")
                .options(new CommandOptionDescriptor[]{
                        ChannelResolver.describe().build()
                })
                .details("Providing a channel will cause the bot to post osu! events for all guild members in said channel.\n"
                        + "If no channel is provided, the bot will stop the feed.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.CHANNEL.resolveOptionOrDefaultIfExists(event, event.getTextChannel(), channel -> acceptChannel(event, channel));
    }

    private void acceptChannel(CommandEvent event, TextChannel channel) {
        if (channel == null) {
            event.getFeedService().unsubscribe(event.getGuild(), Feed.OSU_EVENTS);
            event.reply(CommandReaction.OK, "I will stop posting osu! events on this server.", event::complete);
        } else if (channel.canTalk()) {
            event.getFeedService().subscribe(channel, Feed.OSU_EVENTS);
            event.reply(CommandReaction.OK, "I will start posting osu! events in " + channel.getAsMention() + ".", event::complete);
        } else {
            event.reply(CommandReaction.ERROR, "I cannot post in " + channel.getAsMention() + "!", event::complete);
        }
    }

}
