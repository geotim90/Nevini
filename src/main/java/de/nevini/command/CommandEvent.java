package de.nevini.command;

import de.nevini.util.Paginator;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.function.Consumer;

import static de.nevini.util.Formatter.summarize;

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
        return !isFromType(ChannelType.TEXT) || getGuild().getSelfMember().hasPermission(getTextChannel(),
                Permission.MESSAGE_READ,
                Permission.MESSAGE_WRITE,
                Permission.MESSAGE_HISTORY,
                Permission.MESSAGE_EMBED_LINKS,
                Permission.MESSAGE_ADD_REACTION
        );
    }

    public boolean canReact() {
        return !isFromType(ChannelType.TEXT) || getGuild().getSelfMember().hasPermission(getTextChannel(),
                Permission.MESSAGE_READ,
                Permission.MESSAGE_HISTORY,
                Permission.MESSAGE_ADD_REACTION
        );
    }

    public boolean canTalk() {
        return !isFromType(ChannelType.TEXT) || getGuild().getSelfMember().hasPermission(getTextChannel(),
                Permission.MESSAGE_READ,
                Permission.MESSAGE_WRITE
        );
    }

    public void reply(@NonNull CommandReaction reaction) {
        if (canReact()) {
            addReaction(reaction.getUnicode());
        } else {
            reply(reaction.getUnicode());
        }
    }

    public void reply(@NonNull CommandReaction reaction, @NonNull String content) {
        if (!canTalk() && canReact()) {
            addReaction(reaction.getUnicode());
        }
        reply(reaction.getUnicode() + ' ' + content);
    }

    public void reply(@NonNull EmbedBuilder embed) {
        reply(embed, ignore());
    }

    public void reply(@NonNull EmbedBuilder embed, @NonNull Consumer<? super Message> callback) {
        if (canEmbed()) {
            sendMessage(getChannel(), embed, callback);
        } else {
            replyDm(embed, callback);
        }
    }

    public void reply(@NonNull String content) {
        reply(content, ignore());
    }

    public void reply(@NonNull String content, @NonNull Consumer<? super Message> callback) {
        if (canTalk()) {
            sendMessage(getChannel(), content, callback);
        } else {
            replyDm(content, callback);
        }
    }

    public void replyDm(@NonNull String content) {
        replyDm(content, ignore());
    }

    public void replyDm(@NonNull String content, @NonNull Consumer<? super Message> callback) {
        if (canReact()) {
            addReaction(CommandReaction.DM.getUnicode());
        }
        sendMessage(getAuthor().openPrivateChannel().complete(), content, callback);
    }

    public void replyDm(@NonNull EmbedBuilder embed) {
        replyDm(embed, ignore());
    }

    public void replyDm(@NonNull EmbedBuilder embed, @NonNull Consumer<? super Message> callback) {
        if (canReact()) {
            addReaction(CommandReaction.DM.getUnicode());
        }
        sendMessage(getAuthor().openPrivateChannel().complete(), embed, callback);
    }

    private void addReaction(String unicode) {
        log.info("{} - reaction: {}", getMessageId(), unicode);
        getMessage().addReaction(unicode).queue();
    }

    private void sendMessage(MessageChannel channel, EmbedBuilder embed, Consumer<? super Message> callback) {
        log.info("{} - {}: {}", getMessageId(), channel.getType().name().toLowerCase(), summarize(embed.toString()));
        new Paginator(this, channel, embed, callback).display();
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
