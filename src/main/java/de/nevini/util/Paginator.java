package de.nevini.util;

import de.nevini.command.CommandEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class Paginator {

    // use a page size that is <= 25 and divisible by 2 and 3
    private static final int PAGE_SIZE = 24;

    private static final String EMOTE_BACK = "\u25C0";
    private static final String EMOTE_CANCEL = "\u23F9";
    private static final String EMOTE_NEXT = "\u25B6";

    @NonNull
    private final CommandEvent context;
    @NonNull
    private final MessageChannel channel;
    @NonNull
    private final EmbedBuilder embed;
    @NonNull
    private final Consumer<? super Message> callback;

    private Message container = null;
    private int currentPage = 0;

    public void display() {
        if (embed.getFields().size() <= PAGE_SIZE) {
            channel.sendMessage(embed.build()).queue(callback);
        } else {
            renderPage(1);
        }
    }

    private void renderPage(int pageNumber) {
        if (pageNumber == currentPage) {
            paginate(container, pageNumber);
        } else {
            int pageCount = getPageCount();
            if (pageNumber < 1) {
                renderPage(1);
            } else if (pageNumber > pageCount) {
                renderPage(pageCount);
            } else {
                EmbedBuilder pageBuilder = new EmbedBuilder(embed);
                pageBuilder.clearFields();
                pageBuilder.getFields().addAll(embed.getFields().subList((pageNumber - 1) * PAGE_SIZE,
                        Math.min(embed.getFields().size(), pageNumber * PAGE_SIZE)));
                MessageEmbed page = pageBuilder.build();
                if (container == null) {
                    channel.sendMessage(page).queue(message -> {
                        callback.accept(message);
                        paginate(message, pageNumber);
                    });
                } else {
                    container.editMessage(page).queue(message -> paginate(message, pageNumber));
                }
            }
        }
    }

    private void paginate(Message message, int pageNumber) {
        container = message;
        currentPage = pageNumber;
        message.addReaction(EMOTE_BACK).queue();
        message.addReaction(EMOTE_CANCEL).queue();
        message.addReaction(EMOTE_NEXT).queue();
        context.getEventDispatcher().subscribe(MessageReactionAddEvent.class, event ->
                        message.getIdLong() == event.getMessageIdLong()
                                && context.getAuthor().getIdLong() == event.getUser().getIdLong(),
                this::handleReaction, true,
                1, TimeUnit.MINUTES, this::expire, false
        );
    }

    private void handleReaction(MessageReactionAddEvent event) {
        String emote = event.getReactionEmote().getName();
        if (EMOTE_NEXT.equals(emote)) {
            renderPage(currentPage + 1);
        } else if (EMOTE_BACK.equals(emote)) {
            renderPage(currentPage - 1);
        } else if (EMOTE_CANCEL.equals(emote)) {
            expire();
        }
        event.getReaction().removeReaction(event.getUser()).queue();
    }

    private void expire() {
        if (container != null) {
            container.clearReactions().queue();
        }
    }

    private int getPageCount() {
        return ((embed.getFields().size() - 1) / PAGE_SIZE) + 1;
    }

}
