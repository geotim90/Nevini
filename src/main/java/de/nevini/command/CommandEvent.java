package de.nevini.command;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.function.Consumer;

import static de.nevini.util.FormatUtils.summarize;

@Slf4j
@Value
public class CommandEvent {

    @Delegate
    private final CommandContext context;

    @Delegate(types = {MessageReceivedEvent.class, GenericMessageEvent.class, Event.class})
    private final MessageReceivedEvent event;

    private final CommandOptions options;

    public CommandEvent(@NonNull CommandContext context, @NonNull MessageReceivedEvent event, @NonNull CommandOptions options) {
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

    public boolean isOwner() {
        return getAuthor().getId().equals(context.getOwnerId());
    }

    public boolean canEmbed() {
        return canEmbed(getMessage());
    }

    public boolean canEmbed(Message message) {
        return !message.isFromType(ChannelType.TEXT) || message.getGuild().getSelfMember().hasPermission(
                message.getTextChannel(), Permission.MESSAGE_READ, Permission.MESSAGE_WRITE,
                Permission.MESSAGE_EMBED_LINKS);
    }

    public boolean canReact() {
        return canReact(getMessage());
    }

    public boolean canReact(@NonNull Message message) {
        return !message.isFromType(ChannelType.TEXT) || message.getGuild().getSelfMember().hasPermission(
                message.getTextChannel(), Permission.MESSAGE_READ, Permission.MESSAGE_ADD_REACTION);
    }

    public boolean canTalk() {
        return canTalk(getMessage());
    }

    public boolean canTalk(@NonNull Message message) {
        return !message.isFromType(ChannelType.TEXT) || message.getGuild().getSelfMember().hasPermission(
                message.getTextChannel(), Permission.MESSAGE_READ, Permission.MESSAGE_WRITE);
    }

    public void reply(@NonNull CommandReaction reaction) {
        replyTo(getMessage(), reaction);
    }

    public void reply(@NonNull CommandReaction reaction, @NonNull String content) {
        replyTo(getMessage(), reaction, content);
    }

    public void reply(@NonNull MessageEmbed embed) {
        replyTo(getMessage(), embed);
    }

    public void reply(@NonNull String content) {
        replyTo(getMessage(), content);
    }

    public void reply(@NonNull String content, @NonNull Consumer<? super Message> callback) {
        replyTo(getMessage(), content, callback);
    }

    public void replyDm(@NonNull MessageEmbed embed) {
        replyDm(embed, ignore());
    }

    public void replyDm(@NonNull MessageEmbed embed, @NonNull Consumer<? super Message> callback) {
        sendMessage(getAuthor().openPrivateChannel().complete(), embed, callback);
    }

    public void replyDm(@NonNull String content) {
        replyDm(content, ignore());
    }

    public void replyDm(@NonNull String content, @NonNull Consumer<? super Message> callback) {
        sendMessage(getAuthor().openPrivateChannel().complete(), content, callback);
    }

    public void replyTo(@NonNull Message message, @NonNull CommandReaction reaction) {
        if (canReact(message)) {
            addReaction(message, reaction.getUnicode());
        } else {
            replyTo(message, reaction.getUnicode());
        }
    }

    public void replyTo(@NonNull Message message, @NonNull CommandReaction reaction, @NonNull String content) {
        if (!canTalk(message) && canReact(message)) {
            addReaction(message, reaction.getUnicode());
        }
        replyTo(getMessage(), reaction.getUnicode() + ' ' + content);
    }

    public void replyTo(@NonNull Message message, @NonNull MessageEmbed embed) {
        replyTo(message, embed, ignore());
    }

    public void replyTo(@NonNull Message message, @NonNull MessageEmbed embed,
                        @NonNull Consumer<? super Message> callback) {
        if (canEmbed(message)) {
            sendMessage(message.getChannel(), embed, callback);
        } else {
            if (canReact(message)) {
                addReaction(message, CommandReaction.NEUTRAL.getUnicode());
            }
            replyDm(embed, callback);
        }
    }

    public void replyTo(@NonNull Message message, @NonNull String content) {
        replyTo(message, content, ignore());
    }

    public void replyTo(@NonNull Message message, @NonNull String content,
                        @NonNull Consumer<? super Message> callback) {
        if (canTalk(message)) {
            sendMessage(message.getChannel(), content, callback);
        } else {
            if (canReact(message)) {
                addReaction(message, CommandReaction.NEUTRAL.getUnicode());
            }
            replyDm(content, callback);
        }
    }

    private void addReaction(Message message, String unicode) {
        log.info("{} - reaction: {}", getMessageId(), unicode);
        message.addReaction(unicode).queue();
    }

    private void sendMessage(MessageChannel channel, MessageEmbed embed, Consumer<? super Message> callback) {
        log.info("{} - {}: {}", getMessageId(), channel.getType().name().toLowerCase(), summarize(embed.toJSONObject().toString()));
        channel.sendMessage(embed).queue(callback);
    }

    private void sendMessage(MessageChannel channel, String content, Consumer<? super Message> callback) {
        log.info("{} - {}: {}", getMessageId(), channel.getType().name().toLowerCase(), summarize(content));
        if (content.length() <= Message.MAX_CONTENT_LENGTH) {
            channel.sendMessage(content).queue(callback);
        } else {
            String remainder = content;
            while (remainder.length() > Message.MAX_CONTENT_LENGTH) {
                String part = remainder.substring(0, remainder.lastIndexOf('\n', Message.MAX_CONTENT_LENGTH));
                remainder = remainder.substring(part.length());
                if (remainder.isEmpty()) {
                    channel.sendMessage(part).queue(callback);
                } else {
                    channel.sendMessage(part).queue();
                }
            }
        }
    }

    private static <T> Consumer<T> ignore() {
        return ignore -> {
        };
    }

}
