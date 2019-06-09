package de.nevini.bot.modules.guild.feed;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Feed;
import de.nevini.bot.scope.Node;
import de.nevini.framework.command.CommandOptionDescriptor;
import de.nevini.framework.command.CommandReaction;
import lombok.NonNull;
import net.dv8tion.jda.core.entities.TextChannel;

public class FeedSetCommand extends Command {

    public FeedSetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .aliases(new String[]{"subscribe"})
                .node(Node.GUILD_FEED_SET)
                .description("displays a list of feeds")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.FEED.describe(false, true),
                        Resolvers.CHANNEL.describe()
                })
                .details("Providing a channel will cause the bot to post feed updates in said channel.\n"
                        + "If no channel is provided, the bot will stop the feed.")
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
        Resolvers.CHANNEL.resolveOptionOrDefaultIfExists(event, event.getTextChannel(), channel ->
                acceptFeedAndChannel(event, feed, channel));
    }

    private void acceptFeedAndChannel(CommandEvent event, Feed feed, TextChannel channel) {
        if (channel == null) {
            event.getFeedService().unsubscribe(feed, event.getGuild());
            event.reply(CommandReaction.OK, "I will stop posting " + feed.getName() + " on this server.",
                    event::complete);
        } else if (channel.canTalk()) {
            event.getFeedService().subscribe(feed, channel);
            event.reply(CommandReaction.OK, "I will start posting " + feed.getName() + " in "
                    + channel.getAsMention() + ".", event::complete);
        } else {
            event.reply(CommandReaction.ERROR, "I cannot post in " + channel.getAsMention() + "!",
                    event::complete);
        }
    }

}
