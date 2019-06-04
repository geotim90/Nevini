package de.nevini.bot.modules.guild.feed;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.command.CommandOptionDescriptor;
import de.nevini.bot.db.feed.FeedData;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Feed;
import de.nevini.bot.scope.Node;
import de.nevini.bot.util.Formatter;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedGetCommand extends Command {

    private static final Pattern ALL_FLAG = Pattern.compile("(?i)(?:(?:--|//)all|[-/]a)");

    public FeedGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .node(Node.GUILD_FEED_GET)
                .description("displays a list of feeds")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.FEED.describe(false, true),
                        Resolvers.CHANNEL.describe(),
                        CommandOptionDescriptor.builder()
                                .syntax("--all")
                                .description("Explicitly refers to all possible feeds.")
                                .keyword("--all")
                                .aliases(new String[]{"//all", "-a", "/a"})
                                .build(),
                })
                .details("Providing the `--all` flag will display a list of all possible feeds.\n"
                        + "Providing a feed will display whether a subscription is active and in which channel.\n"
                        + "Providing a channel will display active subscriptions in that channel.\n"
                        + "Providing no options will display a list of active subscriptions on this server.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getOptions().getOptions().stream().map(ALL_FLAG::matcher).anyMatch(Matcher::matches)) {
            displayAll(event);
        } else {
            Resolvers.CHANNEL.resolveOptionOrDefaultIfExists(event, event.getTextChannel(), channel ->
                    acceptChannel(event, channel));
        }
    }

    private void acceptChannel(CommandEvent event, TextChannel channel) {
        Resolvers.FEED.resolveArgumentOrOptionOrInputIfExists(event, feed ->
                acceptChannelAndFeed(event, channel, feed));
    }

    private void acceptChannelAndFeed(CommandEvent event, TextChannel channel, Feed feed) {
        if (channel == null && feed == null) {
            displayServer(event);
        } else if (channel == null) {
            displayFeed(event, feed);
        } else if (feed == null) {
            displayChannel(event, channel);
        } else {
            displayFeedInChannel(event, feed, channel);
        }
    }

    private void displayAll(CommandEvent event) {
        StringBuilder builder = new StringBuilder("Here is a list of all available feed types.\n");
        for (Feed feed : Feed.values()) {
            builder.append("\n**").append(feed.getType()).append("** - ").append(feed.getName());
        }
        event.reply(builder.toString(), event::complete);
    }

    private void displayServer(CommandEvent event) {
        List<FeedData> subscriptions = event.getFeedService().getSubscriptions(event.getGuild());
        if (subscriptions.isEmpty()) {
            event.reply("There are currently no active subscriptions on this server.", event::complete);
        } else {
            StringBuilder builder = new StringBuilder();
            if (subscriptions.size() == 1) {
                builder.append("There is currently 1 active subscription on this server.\n");
            } else {
                builder.append("There are currently ").append(subscriptions.size())
                        .append(" active subscriptions on this server.\n");
            }
            for (FeedData subscription : subscriptions) {
                builder.append("\n**").append(subscription.getType()).append("** in <#")
                        .append(Long.toUnsignedString(subscription.getChannel())).append("> (last updated: ")
                        .append(Formatter.formatTimestamp(subscription.getUts())).append(")");
            }
            event.reply(builder.toString(), event::complete);
        }
    }

    private void displayFeed(CommandEvent event, Feed feed) {
        FeedData subscription = event.getFeedService().getSubscription(event.getGuild(), feed);
        if (subscription == null) {
            event.reply("There is currently no active subscription for " + feed.getName() + " on this server.");
        } else {
            event.reply("There is currently an active subscription for " + feed.getName() + " in <#"
                    + Long.toUnsignedString(subscription.getChannel()) + "> (last updated: "
                    + Formatter.formatTimestamp(subscription.getUts()) + ").");
        }
    }

    private void displayFeedInChannel(CommandEvent event, Feed feed, TextChannel channel) {
        FeedData subscription = event.getFeedService().getSubscription(event.getGuild(), feed);
        if (subscription == null) {
            event.reply("There is currently no active subscription for " + feed.getName() + " in "
                    + channel.getAsMention() + ".");
        } else {
            event.reply("There is currently an active subscription for " + feed.getName() + " in "
                    + channel.getAsMention() + " (last updated: " + Formatter.formatTimestamp(subscription.getUts())
                    + ").");
        }
    }

    private void displayChannel(CommandEvent event, TextChannel channel) {
        List<FeedData> subscriptions = event.getFeedService().getSubscriptions(channel);
        if (subscriptions.isEmpty()) {
            event.reply("There are currently no active subscriptions in " + channel.getAsMention() + ".",
                    event::complete);
        } else {
            StringBuilder builder = new StringBuilder();
            if (subscriptions.size() == 1) {
                builder.append("There is currently 1 active subscription in ").append(channel.getAsMention())
                        .append(".\n");
            } else {
                builder.append("There are currently ").append(subscriptions.size()).append(" active subscriptions in ")
                        .append(channel.getAsMention()).append(".\n");
            }
            for (FeedData subscription : subscriptions) {
                builder.append("\n**").append(subscription.getType()).append("** (last updated: ")
                        .append(Formatter.formatTimestamp(subscription.getUts())).append(")");
            }
            event.reply(builder.toString(), event::complete);
        }
    }

}