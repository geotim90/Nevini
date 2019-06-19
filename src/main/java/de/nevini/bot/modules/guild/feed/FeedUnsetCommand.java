package de.nevini.bot.modules.guild.feed;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Feed;
import de.nevini.bot.scope.Node;
import de.nevini.framework.command.CommandOptionDescriptor;
import de.nevini.framework.command.CommandReaction;

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
