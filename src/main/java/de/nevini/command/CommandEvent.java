package de.nevini.command;

import de.nevini.util.Emote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
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

    @Delegate(types = {MessageReceivedEvent.class, GenericMessageEvent.class, Event.class})
    private final MessageReceivedEvent event;

    @Delegate
    private final CommandContext context;

    private String argument;

    public boolean isOwner() {
        return getAuthor().getId().equals(context.getOwnerId());
    }

    public void reply(@NonNull String message) {
        if (!isFromType(ChannelType.TEXT) || getTextChannel().canTalk()) {
            sendMessage("reply", getChannel(), message, ignore -> {
            });
        }
    }

    public void reply(@NonNull String message, Consumer<? super Message> callback) {
        if (!isFromType(ChannelType.TEXT) || getTextChannel().canTalk()) {
            sendMessage("reply", getChannel(), message, callback);
        }
    }

    public void reply(@NonNull Emote emote, @NonNull String message) {
        reply(getEmoteService().getGuildEmote(getGuild(), emote) + ' ' + message);
    }

    public void replyDm(@NonNull String message) {
        sendMessage("replyDm", getAuthor().openPrivateChannel().complete(), message, ignore -> {
        });
    }

    private void sendMessage(String type, MessageChannel channel, String message, Consumer<? super Message> callback) {
        log.info("{} - {}: {}", event.getMessageId(), type, summarize(message));
        if (message.length() <= Message.MAX_CONTENT_LENGTH) {
            channel.sendMessage(message).queue(callback);
        } else {
            String remainder = message;
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

}
