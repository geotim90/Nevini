package de.nevini.util.message;

import de.nevini.util.concurrent.EventDispatcher;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class LazyMultiEmbed<T> {

    private static final String EMOTE_BACK = "\u25C0";
    private static final String EMOTE_CANCEL = "\u23F9";
    private static final String EMOTE_FIRST = "\u23ee";
    private static final String EMOTE_NEXT = "\u25B6";
    private static final String EMOTE_LAST = "\u23ed";

    /**
     * The {@link MessageChannel} to post this {@link LazyMultiEmbed} in when {@link #display()} is called.
     */
    @NonNull
    private final MessageChannel channel;

    /**
     * The only {@link User} that is allowed to interact with this {@link LazyMultiEmbed}.
     */
    @NonNull
    private final User user;

    /**
     * The icon to use in the footer for paging (can be {@code null}).
     */
    private final String footerIconUrl;

    /**
     * The items to use as a basis for creating {@link EmbedBuilder} instances for individual pages.
     */
    @NonNull
    private final List<T> items;

    /**
     * The function for creating {@link EmbedBuilder} instances from items.
     */
    @NonNull
    private final Function<T, EmbedBuilder> embedBuilderFunction;

    /**
     * The {@link EventDispatcher} to use.
     */
    @NonNull
    private final EventDispatcher eventDispatcher;

    /**
     * A callback for when this {@link LazyMultiEmbed} is first displayed.
     */
    @NonNull
    private final Consumer<? super Message> callback;

    private Message container = null;
    private int currentPage = 0;

    public void display() {
        if (items.size() == 1) {
            channel.sendMessage(embedBuilderFunction.apply(items.get(0)).build()).queue(callback);
        } else if (!items.isEmpty()) {
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
                EmbedBuilder embed = embedBuilderFunction.apply(items.get(pageNumber - 1));
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
        switch (emote) {
            case EMOTE_NEXT:
                renderPage(currentPage + 1);
                break;
            case EMOTE_BACK:
                renderPage(currentPage - 1);
                break;
            case EMOTE_CANCEL:
                expire();
                break;
            case EMOTE_FIRST:
                renderPage(1);
                break;
            case EMOTE_LAST:
                renderPage(getPageCount());
                break;
        }
        event.getReaction().removeReaction(event.getUser()).queue();
    }

    private void expire() {
        if (container != null) {
            container.clearReactions().queue();
        }
    }

    private int getPageCount() {
        return items.size();
    }

}
