package de.nevini.command;

import de.nevini.util.Emote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.function.Consumer;

import static de.nevini.util.FormatUtils.summarize;

@Slf4j
@Data
@AllArgsConstructor
public class CommandEvent {

    @Delegate
    private final CommandContext context;

    @Delegate(types = {MessageReceivedEvent.class, GenericMessageEvent.class, Event.class})
    private final MessageReceivedEvent event;

    private String argument;

    public boolean isOwner() {
        return getAuthor().getId().equals(context.getOwnerId());
    }

    public boolean canReact() {
        return canReact(getMessage());
    }

    public boolean canReact(@NonNull Message message) {
        return !message.isFromType(ChannelType.TEXT) || message.getGuild().getSelfMember().hasPermission(
                message.getTextChannel(), Permission.MESSAGE_ADD_REACTION);
    }

    public boolean canTalk() {
        return canTalk(getMessage());
    }

    public boolean canTalk(@NonNull Message message) {
        return !message.isFromType(ChannelType.TEXT) || message.getTextChannel().canTalk();
    }

    public void reply(@NonNull Emote emote) {
        replyTo(getMessage(), emote);
    }

    public void reply(@NonNull Emote emote, @NonNull String content) {
        replyTo(getMessage(), emote, content);
    }

    public void reply(@NonNull String content) {
        replyTo(getMessage(), content);
    }

    public void reply(@NonNull String content, @NonNull Consumer<? super Message> callback) {
        replyTo(getMessage(), content, callback);
    }

    public void replyDm(@NonNull String content) {
        replyDm(content, ignore());
    }

    public void replyDm(@NonNull String content, @NonNull Consumer<? super Message> callback) {
        sendMessage(getAuthor().openPrivateChannel().complete(), content, callback);
    }

    public void replyTo(@NonNull Message message, @NonNull Emote emote) {
        String unicode = getEmoteService().getGuildEmote(getGuild(), emote);
        if (canReact()) {
            addReaction(message, unicode);
        } else {
            replyTo(message, unicode);
        }
    }

    public void replyTo(@NonNull Message message, @NonNull Emote emote, @NonNull String content) {
        String unicode = getEmoteService().getGuildEmote(getGuild(), emote);
        if (!canTalk(message) && canReact(message)) {
            addReaction(message, unicode);
        }
        replyTo(getMessage(), unicode + ' ' + content);
    }

    public void replyTo(@NonNull Message message, @NonNull String content) {
        replyTo(message, content, ignore());
    }

    public void replyTo(@NonNull Message message, @NonNull String content,
                        @NonNull Consumer<? super Message> callback) {
        if (canTalk(message)) {
            sendMessage(getChannel(), content, callback);
        } else {
            if (canReact(message)) {
                addReaction(message, getEmoteService().getGuildEmote(getGuild(), Emote.NEUTRAL));
            }
            replyDm(content, callback);
        }
    }

    private void addReaction(Message message, String unicode) {
        log.info("{} - reaction: {}", getMessageId(), unicode);
        message.addReaction(unicode).queue();
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
