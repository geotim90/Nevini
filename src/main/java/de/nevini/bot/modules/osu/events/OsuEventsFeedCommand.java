package de.nevini.bot.modules.osu.events;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.command.CommandOptionDescriptor;
import de.nevini.bot.modules.guild.feed.FeedSetCommand;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Feed;
import de.nevini.bot.scope.Node;

public class OsuEventsFeedCommand extends Command {

    private final FeedSetCommand delegate = new FeedSetCommand();

    public OsuEventsFeedCommand() {
        super(CommandDescriptor.builder()
                .keyword("feed")
                .aliases(new String[]{"subscribe"})
                .node(Node.OSU_EVENTS_FEED)
                .description("subscribes to osu! events")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.CHANNEL.describe()
                })
                .details("Providing a channel will cause the bot to post osu! events "
                        + "for all guild members in said channel.\n"
                        + "If no channel is provided, the bot will stop the feed.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.executeExternal(event, Feed.OSU_EVENTS);
    }

}
