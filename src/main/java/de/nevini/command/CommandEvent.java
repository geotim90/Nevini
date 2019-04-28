package de.nevini.command;

import de.nevini.util.Emote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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
            // TODO split message if longer than 2000 characters
            log.info("{} - reply: {}", event.getMessageId(), summarize(message));
            getChannel().sendMessage(message).queue();
        }
    }

    public void reply(@NonNull Emote emote, @NonNull String message) {
        reply(getEmoteService().getGuildEmote(getGuild(), emote) + ' ' + message);
    }

    public void replyDm(String message) {
        // TODO split message if longer than 2000 characters
        log.info("{} - replyDm: {}", event.getMessageId(), summarize(message));
        getAuthor().openPrivateChannel().complete().sendMessage(message).queue();
    }

}
