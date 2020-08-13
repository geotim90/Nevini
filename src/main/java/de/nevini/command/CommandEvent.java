package de.nevini.command;

import de.nevini.scope.Permissions;
import de.nevini.util.command.CommandOptions;
import de.nevini.util.command.CommandReaction;
import de.nevini.util.message.MessageCleaner;
import de.nevini.util.message.MessageLineSplitter;
import de.nevini.util.message.PageableEmbed;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.Instant;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.nevini.util.Formatter.summarize;
import static de.nevini.util.Functions.ignore;

@Slf4j
@Value
public class CommandEvent {

    private static final Pattern DM_FLAG = Pattern.compile("(?i)(?:--|//)dm");
    private static final Pattern RM_FLAG = Pattern.compile("(?i)(?:--|//)rm");

    @Delegate
    CommandContext context;

    @Delegate(types = {MessageReceivedEvent.class, GenericMessageEvent.class, Event.class})
    MessageReceivedEvent event;

    CommandOptions options;

    public CommandEvent(
            @NonNull CommandContext context, @NonNull MessageReceivedEvent event, @NonNull CommandOptions options
    ) {
        this.context = context;
        this.event = event;
        this.options = options;
    }

    public String getArgument() {
        return options.getArgument().orElse(null);
    }

    public CommandEvent withArgument(String argument) {
        return new CommandEvent(context, event, options.withArgument(argument));
    }

    public boolean isBotOwner() {
        return getAuthor().getId().equals(context.getOwnerId());
    }

    /**
     * Adds a reaction to {@link #getMessage()} indicating that a long task is being executed.
     * Requires {@link Permissions#REACT}.
     */
    public void notifyLongTaskStart() {
        getMessage().addReaction(CommandReaction.WAIT.getUnicode()).queue();
    }

    /**
     * Removes all reactions from {@link #getMessage()}.
     * Only works with {@link Permission#MESSAGE_MANAGE}.
     */
    public void notifyLongTaskEnd() {
        if (isFromGuild() && getGuild().getSelfMember().hasPermission(getTextChannel(), Permission.MESSAGE_MANAGE)) {
            getMessage().clearReactions().queue();
        }
    }

    /**
     * Provides a new {@link EmbedBuilder} with some default customisations.
     * <ul>
     * <li>The guild name and icon will be used as the "author".</li>
     * <li>The self-member color, effective name and icon will be used as the "footer".</li>
     * <li>The current system time will be used as the "timestamp".</li>
     * </ul>
     */
    public EmbedBuilder createEmbedBuilder() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (isFromGuild()) {
            embedBuilder.setAuthor(getGuild().getName(), null, getGuild().getIconUrl());
            embedBuilder.setColor(getGuild().getSelfMember().getColor());
            embedBuilder.setFooter(getGuild().getSelfMember().getEffectiveName(),
                    getJDA().getSelfUser().getAvatarUrl());
        } else {
            embedBuilder.setAuthor(getJDA().getSelfUser().getName(), null, getJDA().getSelfUser().getAvatarUrl());
            embedBuilder.setColor(Color.WHITE);
        }
        embedBuilder.setTimestamp(Instant.now());
        return embedBuilder;
    }

    /**
     * Adds a reaction to {@link #getMessage()}.
     * Requires {@link Permissions#REACT}.
     *
     * @param reaction the {@link CommandReaction} to use
     * @param callback the success callback that will be called with {@link #getMessage()}
     * @see Message#addReaction(String)
     */
    public void reply(@NonNull CommandReaction reaction, @NonNull Consumer<? super Message> callback) {
        addReaction(reaction.getUnicode(), callback);
    }

    /**
     * Replies with a message that may be split up into multiple messages if too long.
     * Requires {@link Permissions#TALK}.
     * A {@code --dm} flag will force the message to be sent via direct message.
     *
     * @param content  the contents of the message
     * @param callback the success callback that will be called with the new {@link Message}
     */
    public void reply(@NonNull String content, @NonNull Consumer<? super Message> callback) {
        if (isDm()) {
            replyDm(content, callback);
        } else {
            sendMessage(getChannel(), content, callback);
        }
    }

    /**
     * Replies with a message that starts with an icon and may be split up into multiple messages if too long.
     * Requires {@link Permissions#TALK}.
     * A {@code --dm} flag will force the message to be sent via direct message.
     *
     * @param reaction the {@link CommandReaction} to use in the message
     * @param content  the contents of the message
     * @param callback the success callback that will be called with the new {@link Message}
     */
    public void reply(
            @NonNull CommandReaction reaction, @NonNull String content, @NonNull Consumer<? super Message> callback
    ) {
        reply(reaction.getUnicode() + ' ' + content, callback);
    }

    /**
     * Replies with an embed that may require paging depending on the number of fields.
     * Requires {@link Permissions#BOT_EMBED}.
     * A {@code --dm} flag will cause an error message to be sent via direct message.
     *
     * @param embed    the {@link EmbedBuilder} to create the embed with.
     * @param callback the success callback that will be called with the new {@link Message}
     */
    public void reply(@NonNull EmbedBuilder embed, @NonNull Consumer<? super Message> callback) {
        if (isDm()) {
            reply(CommandReaction.WARNING, "You cannot use this command with `--dm`!", this::complete);
        } else {
            sendMessage(getChannel(), embed, callback);
        }
    }

    /**
     * Replies with a direct message that may be split up into multiple messages if too long.
     * This method will also add a reaction to the command in the guild, if possible.
     *
     * @param content  the contents of the message
     * @param callback the success callback that will be called with the new {@link Message}
     */
    public void replyDm(@NonNull String content, @NonNull Consumer<? super Message> callback) {
        if (isFromGuild() && getGuild().getSelfMember().hasPermission(getTextChannel(), Permissions.REACT)) {
            addReaction(CommandReaction.DM.getUnicode(), ignore());
        }
        getAuthor().openPrivateChannel().queue(channel -> sendMessage(channel, content, callback));
    }

    /**
     * Replies with a message that may be split up into multiple messages if too long.
     * Requires {@link Permissions#TALK}.
     * A {@code --dm} flag will cause an error message to be sent via direct message.
     *
     * @param content  the contents of the message
     * @param callback the success callback that will be called with the new {@link Message}
     */
    public void replyNoDm(@NonNull String content, @NonNull Consumer<? super Message> callback) {
        if (isDm()) {
            reply(CommandReaction.WARNING, "You cannot use this command with `--dm`!", this::complete);
        } else {
            sendMessage(getChannel(), content, callback);
        }
    }

    /**
     * Adds a reaction to {@link #getMessage()}.
     * Requires {@link Permissions#REACT}.
     *
     * @param unicode  the unicode emoji to use
     * @param callback the success callback that will be called with {@link #getMessage()}
     * @see Message#addReaction(Emote)
     */
    private void addReaction(String unicode, Consumer<? super Message> callback) {
        log.info("{} - reaction: {}", getMessageId(), unicode);
        getMessage().addReaction(unicode).queue(ignore -> callback.accept(getMessage()));
    }

    /**
     * Sends a message that may be split up into multiple messages if too long.
     * Requires {@link Permissions#TALK}.
     *
     * @param channel  the {@link MessageChannel} to send the message in
     * @param content  the contents of the message
     * @param callback the success callback that will be called with the new {@link Message}
     * @see MessageLineSplitter
     */
    private void sendMessage(MessageChannel channel, String content, Consumer<? super Message> callback) {
        log.info("{} - {}: {}", getMessageId(), channel.getType().name().toLowerCase(), summarize(content));
        MessageLineSplitter.sendMessage(channel, content, callback);
    }

    /**
     * Sends an embed that may require paging depending on the number of fields.
     * Requires {@link Permissions#BOT_EMBED}.
     *
     * @param channel  the {@link MessageChannel} to send the message in
     * @param embed    the {@link EmbedBuilder} to create the embed with.
     * @param callback the success callback that will be called with the new {@link Message}
     * @see PageableEmbed
     */
    private void sendMessage(MessageChannel channel, EmbedBuilder embed, Consumer<? super Message> callback) {
        log.info("{} - {}: {}", getMessageId(), channel.getType().name().toLowerCase(), summarize(embed.toString()));
        new PageableEmbed(
                channel, getAuthor(), getJDA().getSelfUser().getAvatarUrl(), embed, getEventDispatcher(), callback
        ).display();
    }

    /**
     * Cleans up after a command has finished executing.
     * There is no need to call this method if no messages can/should be removed.
     * This method will wait one minute before deleting any messages to give the user a chance to see the last response.
     *
     * @see #complete(Message, boolean)
     */
    public void complete() {
        complete(getMessage(), false);
    }

    /**
     * Cleans up after a command has finished executing.
     * This method is typically used as a {@code Consumer<? super Message>} callback ({@code event::complete}).
     * There is no need to call this method if no messages can/should be removed.
     * This method will wait one minute before deleting {@code lastResponse} to give the user a chance to see it.
     *
     * @param lastResponse the last {@link Message} related to this command (can be equal to {@link #getMessage()})
     */
    public void complete(@NonNull Message lastResponse) {
        complete(lastResponse, false);
    }

    /**
     * The same as {@link #complete()}, but can force message removal even if no {@code --rm} flag is present.
     * Note that messages may not be able to be deleted due to permissions. This will not cause any errors.
     * This method will wait one minute before deleting any messages to give the user a chance to see the last response.
     * If messages need to be deleted immediately, use {@link MessageCleaner#tryDelete(Message)} instead.
     *
     * @param forceRm whether to force message removal
     */
    public void complete(boolean forceRm) {
        complete(getMessage(), forceRm);
    }

    /**
     * The same as {@link #complete(Message)}, but can force message removal even if no {@code --rm} flag is present.
     * Note that messages may not be able to be deleted due to permissions. This will not cause any errors.
     * This method will wait one minute before deleting {@code lastMessage} to give the user a chance to see it.
     * If messages need to be deleted immediately, use {@link MessageCleaner#tryDelete(Message)} instead.
     *
     * @param lastResponse the last {@link Message} related to this command (can be equal to {@link #getMessage()})
     * @param forceRm      whether to force message removal
     */
    public void complete(@NonNull Message lastResponse, boolean forceRm) {
        if (forceRm || isRm()) {
            if (getMessage().getIdLong() != lastResponse.getIdLong()) {
                MessageCleaner.tryDelete(getMessage());
            }
            // give the user a chance to see the last response before deleting it
            MessageCleaner.tryScheduleDelete(getEventDispatcher(), lastResponse);
        }
    }

    /**
     * Returns {@code true} if {@link #getOptions()} contains a {@link #DM_FLAG} and the command is from a guild.
     * This will force {@link #reply(String, Consumer)} and {@link #reply(CommandReaction, String, Consumer)}
     * to respond using direct messages.
     */
    public boolean isDm() {
        return isFromType(ChannelType.TEXT)
                && getOptions().getOptions().stream().map(DM_FLAG::matcher).anyMatch(Matcher::matches);
    }

    /**
     * Returns {@code true} if {@link #getOptions()} contains a {@link #RM_FLAG} and will cause
     * {@link #complete(Message, boolean)} (and the other {@code complete} methods) to attempt to delete messages.
     */
    public boolean isRm() {
        return getOptions().getOptions().stream().map(RM_FLAG::matcher).anyMatch(Matcher::matches);
    }

}
