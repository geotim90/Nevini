package de.nevini.framework.message;

import de.nevini.commons.concurrent.EventDispatcher;
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
import java.util.function.Function;

@RequiredArgsConstructor
public class PickerEmbed<T> {

    /**
     * The maximum number of items supported by {@link PickerEmbed}.
     */
    public static final int MAX = 10;

    private static final String[] EMOTE_NUMBER = {
            "1\u20E3", "2\u20E3", "3\u20E3", "4\u20E3", "5\u20E3",
            "6\u20E3", "7\u20E3", "8\u20E3", "9\u20E3", "\uD83D\uDD1F"
    };

    /**
     * The {@link MessageChannel} to post this {@link PickerEmbed} in when {@link #display()} is called.
     */
    @NonNull
    private final MessageChannel channel;

    /**
     * The only {@link User} that is allowed to interact with this {@link PageableEmbed}.
     */
    @NonNull
    private final User user;

    /**
     * The {@link EmbedBuilder} to use.
     * The description and fields will be replaced.
     */
    @NonNull
    private final EmbedBuilder embedBuilder;

    /**
     * A {@link List} of up to {@value #MAX} items for the user to choose from.
     */
    @NonNull
    private final List<T> items;

    /**
     * What field name to display per item.
     * Must not return {@code null}!
     */
    @NonNull
    private final Function<T, String> fieldNameRenderer;

    /**
     * What field value to display per item.
     * Must not return {@code null}!
     */
    @NonNull
    private final Function<T, String> fieldValueRenderer;

    /**
     * The {@link EventDispatcher} to use.
     */
    @NonNull
    private final EventDispatcher eventDispatcher;

    /**
     * Callback for when an item was chosen.
     */
    @NonNull
    private final Consumer<T> callback;

    /**
     * Callback for when no item was chosen within one minute.
     */
    @NonNull
    private final Runnable timeoutCallback;

    private void checkLength() {
        if (items.size() > MAX) {
            throw new IllegalStateException(
                    getClass().getSimpleName() + " only supports up to " + MAX + " items!"
            );
        }
    }

    public void display() {
        checkLength();
        embedBuilder.setDescription("Please pick one of the following options by selecting a reaction below:");
        embedBuilder.clearFields();
        for (int i = 0; i < items.size(); i++) {
            embedBuilder.addField(EMOTE_NUMBER[i] + " - " + fieldNameRenderer.apply(items.get(i)),
                    fieldValueRenderer.apply(items.get(i)), false);
        }
        MessageEmbed embed = embedBuilder.build();
        channel.sendMessage(embed).queue(this::decorate);
    }

    private void decorate(Message message) {
        for (int i = 0; i < items.size(); i++) {
            message.addReaction(EMOTE_NUMBER[i]).queue();
        }
        eventDispatcher.subscribe(MessageReactionAddEvent.class, event ->
                        message.getIdLong() == event.getMessageIdLong()
                                && user.getIdLong() == event.getUser().getIdLong(),
                event -> handleReaction(message, event), true,
                1, TimeUnit.MINUTES, () -> expire(message), false
        );
    }

    private void handleReaction(Message message, MessageReactionAddEvent event) {
        for (int i = 0; i < items.size(); i++) {
            if (EMOTE_NUMBER[i].equals(event.getReactionEmote().getName())) {
                MessageCleaner.tryDelete(message);
                callback.accept(items.get(i));
                return;
            }
        }
        event.getReaction().removeReaction(event.getUser()).queue();
    }

    private void expire(Message message) {
        MessageCleaner.tryDelete(message);
        timeoutCallback.run();
    }

}
