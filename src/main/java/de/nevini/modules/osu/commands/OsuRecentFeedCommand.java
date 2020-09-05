package de.nevini.modules.osu.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Feed;
import de.nevini.core.scope.Node;
import de.nevini.core.scope.Permissions;
import de.nevini.modules.guild.feed.commands.FeedSetCommand;
import de.nevini.util.command.CommandOptionDescriptor;

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
