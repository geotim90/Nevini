package de.nevini.modules.guild.feed.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Feed;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;

class FeedUnsetCommand extends Command {

    FeedUnsetCommand() {
        super(CommandDescriptor.builder()
                .keyword("unset")
                .aliases(new String[]{"remove", "stop", "unsubscribe", "unsub", "-"})
                .node(Node.GUILD_FEED_SET)
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
