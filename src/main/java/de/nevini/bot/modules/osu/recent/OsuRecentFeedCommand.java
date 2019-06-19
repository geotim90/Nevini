package de.nevini.bot.modules.osu.recent;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.modules.guild.feed.FeedSetCommand;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Feed;
import de.nevini.bot.scope.Node;
import de.nevini.bot.scope.Permissions;
import de.nevini.framework.command.CommandOptionDescriptor;

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
