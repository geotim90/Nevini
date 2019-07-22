package de.nevini.util.message;

import de.nevini.util.concurrent.EventDispatcher;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class PageableEmbed {

    // use a page size that is <= 25 and divisible by 2 and 3 for column layouts
    private static final int INLINE_PAGE_SIZE = 24;
    // use a smaller page size for single column content
    private static final int BLOCK_PAGE_SIZE = 10;

    private static final String EMOTE_BACK = "\u25C0";
    private static final String EMOTE_CANCEL = "\u23F9";
    private static final String EMOTE_FIRST = "\u23ee";
    private static final String EMOTE_NEXT = "\u25B6";
    private static final String EMOTE_LAST = "\u23ed";

    /**
     * The {@link MessageChannel} to post this {@link PageableEmbed} in when {@link #display()} is called.
     */
    @NonNull
    private final MessageChannel channel;

    /**
     * The only {@link User} that is allowed to interact with this {@link PageableEmbed}.
     */
    @NonNull
    private final User user;

    /**
     * The icon to use in the footer for paging (can be {@code null}).
     */
    private final String footerIconUrl;

    /**
     * An {@link EmbedBuilder} with potentially more fields than can be displayed at once.
     */
    @NonNull
    private final EmbedBuilder embed;

    /**
     * The {@link EventDispatcher} to use.
     */
    @NonNull
    private final EventDispatcher eventDispatcher;

    /**
     * A callback for when this {@link PageableEmbed} is first displayed.
     */
    @NonNull
    private final Consumer<? super Message> callback;

    private MessageEmbed template = null;
    private Message container = null;
    private int currentPage = 0;

    public void display() {
        if (embed.getFields().size() <= getPageSize()) {
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
                ensureTemplate();
                EmbedBuilder pageBuilder = new EmbedBuilder(template);
                pageBuilder.getFields().addAll(embed.getFields().subList((pageNumber - 1) * getPageSize(),
                        Math.min(embed.getFields().size(), pageNumber * getPageSize())));
                pageBuilder.setFooter("Page " + pageNumber + "/" + pageCount, footerIconUrl);
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

    private void ensureTemplate() {
        if (template == null) {
            EmbedBuilder templateBuilder = new EmbedBuilder(embed);
            templateBuilder.clearFields();
            template = templateBuilder.build();
        }
    }

    private void paginate(Message message, int pageNumber) {
        container = message;
        currentPage = pageNumber;
        message.addReaction(EMOTE_FIRST).queue();
        message.addReaction(EMOTE_BACK).queue();
        message.addReaction(EMOTE_CANCEL).queue();
        message.addReaction(EMOTE_NEXT).queue();
        message.addReaction(EMOTE_LAST).queue();
        eventDispatcher.subscribe(MessageReactionAddEvent.class, event ->
                        message.getIdLong() == event.getMessageIdLong()
                                && user.getIdLong() == event.getUser().getIdLong(),
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
        } else if (EMOTE_FIRST.equals(emote)) {
            renderPage(1);
        } else if (EMOTE_LAST.equals(emote)) {
            renderPage(getPageCount());
        }
        event.getReaction().removeReaction(event.getUser()).queue();
    }

    private void expire() {
        if (container != null) {
            container.clearReactions().queue();
        }
    }

    private int getPageCount() {
        return ((embed.getFields().size() - 1) / getPageSize()) + 1;
    }

    private int getPageSize() {
        if (embed.getFields().isEmpty() || !embed.getFields().get(0).isInline()) {
            return BLOCK_PAGE_SIZE;
        } else {
            return INLINE_PAGE_SIZE;
        }
    }

}
