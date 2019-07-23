package de.nevini.modules.guild.feed;

import de.nevini.command.*;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Feed;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;

class FeedUnsetCommand extends Command {

    FeedUnsetCommand() {
        super(CommandDescriptor.builder()
                .keyword("unset")
                .aliases(new String[]{"remove", "stop", "unsubscribe", "unsub", "-"})
                .node(Node.GUILD_FEED_UNSET)
                .description("stops automatic feeds")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.FEED.describe(false, true)
                })
                .details("This command will stop automatic feed updates for a specific feed.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.FEED.resolveArgumentOrOptionOrInput(event, feed -> acceptFeed(event, feed));
    }

    private void acceptFeed(CommandEvent event, Feed feed) {
        event.getFeedService().unsubscribe(feed, event.getGuild());
        event.reply(CommandReaction.OK, "I will stop posting " + feed.getName() + " on this server.",
                event::complete);
    }

}
