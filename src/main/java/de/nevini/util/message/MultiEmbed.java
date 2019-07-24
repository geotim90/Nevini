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

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class MultiEmbed {

    private static final String EMOTE_BACK = "\u25C0";
    private static final String EMOTE_CANCEL = "\u23F9";
    private static final String EMOTE_FIRST = "\u23ee";
    private static final String EMOTE_NEXT = "\u25B6";
    private static final String EMOTE_LAST = "\u23ed";

    /**
     * The {@link MessageChannel} to post this {@link MultiEmbed} in when {@link #display()} is called.
     */
    @NonNull
    private final MessageChannel channel;

    /**
     * The only {@link User} that is allowed to interact with this {@link MultiEmbed}.
     */
    @NonNull
    private final User user;

    /**
     * The icon to use in the footer for paging (can be {@code null}).
     */
    private final String footerIconUrl;

    /**
     * The {@link EmbedBuilder} instances to use for rendering individual pages.
     */
    @NonNull
    private final List<EmbedBuilder> embeds;

    /**
     * The {@link EventDispatcher} to use.
     */
    @NonNull
    private final EventDispatcher eventDispatcher;

    /**
     * A callback for when this {@link MultiEmbed} is first displayed.
     */
    @NonNull
    private final Consumer<? super Message> callback;

    private Message container = null;
    private int currentPage = 0;

    public void display() {
        if (embeds.size() == 1) {
            channel.sendMessage(embeds.get(0).build()).queue(callback);
        } else if (!embeds.isEmpty()) {
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
                EmbedBuilder embed = embeds.get(pageNumber - 1);
                embed.setFooter("Page " + pageNumber + "/" + pageCount, footerIconUrl);
                MessageEmbed page = embed.build();
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
        if (container == null) {
            message.addReaction(EMOTE_FIRST).queue();
            message.addReaction(EMOTE_BACK).queue();
            message.addReaction(EMOTE_CANCEL).queue();
            message.addReaction(EMOTE_NEXT).queue();
            message.addReaction(EMOTE_LAST).queue();
        }

        container = message;
        currentPage = pageNumber;

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
        return embeds.size();
    }

}
