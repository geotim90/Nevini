package de.nevini.command;

import de.nevini.util.Cleaner;
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
        reply(reaction, ignore());
    }

    public void reply(@NonNull CommandReaction reaction, @NonNull Consumer<? super Message> callback) {
        if (canReact()) {
            addReaction(reaction.getUnicode(), callback);
        } else {
            reply(reaction.getUnicode(), callback);
        }
    }

    public void reply(@NonNull CommandReaction reaction, @NonNull String content) {
        reply(reaction, content, ignore());
    }

    public void reply(@NonNull CommandReaction reaction, @NonNull String content, @NonNull Consumer<? super Message> callback) {
        if (!canTalk() && canReact()) {
            addReaction(reaction.getUnicode(), ignore());
        }
        reply(reaction.getUnicode() + ' ' + content, callback);
    }

    public void reply(@NonNull EmbedBuilder embed) {
        reply(embed, ignore());
    }

    public void reply(@NonNull EmbedBuilder embed, @NonNull Consumer<? super Message> callback) {
        if (canEmbed() && !isDm()) {
            sendMessage(getChannel(), embed, callback);
        } else {
            replyDm(embed, callback);
        }
    }

    public void reply(@NonNull String content) {
        reply(content, ignore());
    }

    public void reply(@NonNull String content, @NonNull Consumer<? super Message> callback) {
        if (canTalk() && !isDm()) {
            sendMessage(getChannel(), content, callback);
        } else {
            replyDm(content, callback);
        }
    }

    public void replyDm(@NonNull String content) {
        replyDm(content, ignore());
    }

    public void replyDm(@NonNull String content, @NonNull Consumer<? super Message> callback) {
        if (isFromType(ChannelType.TEXT) && canReact()) {
            addReaction(CommandReaction.DM.getUnicode(), ignore());
        }
        sendMessage(getAuthor().openPrivateChannel().complete(), content, callback);
    }

    public void replyDm(@NonNull EmbedBuilder embed) {
        replyDm(embed, ignore());
    }

    public void replyDm(@NonNull EmbedBuilder embed, @NonNull Consumer<? super Message> callback) {
        if (isFromType(ChannelType.TEXT) && canReact()) {
            addReaction(CommandReaction.DM.getUnicode(), ignore());
        }
        sendMessage(getAuthor().openPrivateChannel().complete(), embed, callback);
    }

    private void addReaction(String unicode, Consumer<? super Message> callback) {
        log.info("{} - reaction: {}", getMessageId(), unicode);
        getMessage().addReaction(unicode).queue(ignore -> callback.accept(getMessage()));
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

    public EmbedBuilder createEmbedBuilder() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (getGuild() != null) {
            embedBuilder.setAuthor(getGuild().getName(), null, getGuild().getIconUrl());
            embedBuilder.setColor(getGuild().getSelfMember().getColor());
            embedBuilder.setFooter(getGuild().getSelfMember().getEffectiveName(), getJDA().getSelfUser().getAvatarUrl());
        } else {
            embedBuilder.setAuthor(getJDA().getSelfUser().getName(), null, getJDA().getSelfUser().getAvatarUrl());
            embedBuilder.setColor(Color.BLUE);
        }
        embedBuilder.setTimestamp(Instant.now());
        return embedBuilder;
    }

    public void complete() {
        complete(getMessage(), false);
    }

    public void complete(@NonNull Message lastResponse) {
        complete(lastResponse, false);
    }

    public void complete(boolean forceRm) {
        complete(getMessage(), forceRm);
    }

    public void complete(@NonNull Message lastResponse, boolean forceRm) {
        if (forceRm || isRm()) {
            if (lastResponse.equals(getMessage())) {
                if (lastResponse.getReactions().isEmpty()) {
                    Cleaner.tryDelete(lastResponse);
                } else {
                    Cleaner.tryScheduleDelete(getEventDispatcher(), lastResponse);
                }
            } else {
                Cleaner.tryDelete(getMessage());
                Cleaner.tryScheduleDelete(getEventDispatcher(), lastResponse);
            }
        }
    }

    private boolean isDm() {
        return getOptions().getOptions().stream().map(DM_FLAG::matcher).anyMatch(Matcher::matches);
    }

    private boolean isRm() {
        return getOptions().getOptions().stream().map(RM_FLAG::matcher).anyMatch(Matcher::matches);
    }

}
