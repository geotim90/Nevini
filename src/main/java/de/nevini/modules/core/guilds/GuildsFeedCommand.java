package de.nevini.modules.core.guilds;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.guild.feed.FeedSetCommand;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Feed;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;

class GuildsFeedCommand extends Command {

    private final FeedSetCommand delegate = new FeedSetCommand();

    GuildsFeedCommand() {
        super(CommandDescriptor.builder()
                .keyword("feed")
                .aliases(new String[]{"subscribe"})
                .ownerOnly(true)
                .node(Node.CORE_HELP) // dummy node
                .description("subscribes to bot guild events")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.CHANNEL.describe()
                })
                .details("This command will behave the same as **feed set guilds**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.executeExternal(event, Feed.GUILDS);
    }

}
