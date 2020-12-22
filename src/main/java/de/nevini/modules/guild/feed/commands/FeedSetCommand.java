package de.nevini.modules.guild.feed.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Feed;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.TextChannel;

public class FeedSetCommand extends Command {

    public FeedSetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .aliases(new String[]{"add", "start", "subscribe", "sub", "+"})
                .node(Node.GUILD_FEED_SET)
                .description("starts automatic feeds")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.FEED.describe(false, true),
                        Resolvers.CHANNEL.describe()
                })
                .details("This command will cause the bot to post automatic feed updates in the provided channel.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.FEED.resolveArgumentOrOptionOrInput(event, feed -> acceptFeed(event, feed));
    }

    /**
     * Method for feed-specific commands from other modules to delegate to this command.<br>
     * Note that the caller <b>must</b> properly check permissions first
     * (e.g. by using a node that points to {@link Node#GUILD_FEED_SET})!
     */
    public void executeExternal(@NonNull CommandEvent event, @NonNull Feed feed) {
        acceptFeed(event, feed);
    }

    private void acceptFeed(CommandEvent event, Feed feed) {
        Resolvers.CHANNEL.resolveOptionOrDefault(event, event.getTextChannel(), channel ->
                acceptFeedAndChannel(event, feed, channel));
    }

    private void acceptFeedAndChannel(CommandEvent event, Feed feed, TextChannel channel) {
        if (channel.canTalk()) {
            event.getFeedService().subscribe(feed, channel);
            event.reply(CommandReaction.OK, "I will start posting " + feed.getName() + " in "
                    + channel.getAsMention() + ".", event::complete);
        } else {
            event.reply(CommandReaction.ERROR, "I cannot post in " + channel.getAsMention() + "!",
                    event::complete);
        }
    }

}
