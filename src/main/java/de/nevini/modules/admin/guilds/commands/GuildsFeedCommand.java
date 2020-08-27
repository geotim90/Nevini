package de.nevini.modules.admin.guilds.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Feed;
import de.nevini.core.scope.Node;
import de.nevini.modules.guild.feed.commands.FeedSetCommand;
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
