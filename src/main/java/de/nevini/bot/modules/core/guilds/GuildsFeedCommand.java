package de.nevini.bot.modules.core.guilds;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.modules.guild.feed.FeedSetCommand;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Feed;
import de.nevini.bot.scope.Node;
import de.nevini.framework.command.CommandOptionDescriptor;

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
