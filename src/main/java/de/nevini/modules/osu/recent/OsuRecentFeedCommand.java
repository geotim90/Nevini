package de.nevini.modules.osu.recent;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.modules.guild.feed.FeedSetCommand;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Feed;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;

class OsuRecentFeedCommand extends Command {

    private final FeedSetCommand delegate = new FeedSetCommand();

    OsuRecentFeedCommand() {
        super(CommandDescriptor.builder()
                .keyword("feed")
                .aliases(new String[]{"subscribe"})
                .node(Node.OSU_RECENT_FEED)
                .minimumBotPermissions(Permissions.BOT_EMBED_EXT)
                .description("subscribes to osu! plays")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.CHANNEL.describe()
                })
                .details("This command will behave the same as **feed set osu.recent**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.executeExternal(event, Feed.OSU_RECENT);
    }

}
