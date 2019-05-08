package de.nevini.util;

import de.nevini.command.CommandEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class Picker<T> {

    public static final int MAX = 9;

    private static final String[] EMOTE_NUMBER = {
            "1\u20E3", "2\u20E3", "3\u20E3",
            "4\u20E3", "5\u20E3", "6\u20E3",
            "7\u20E3", "8\u20E3", "9\u20E3"
    };

    @NonNull
    private final CommandEvent context;
    @NonNull
    private final List<T> items;
    @NonNull
    private final Function<T, String> fieldNameRenderer;
    @NonNull
    private final Function<T, String> fieldValueRenderer;
    @NonNull
    private final Consumer<T> callback;
    @NonNull
    private final Runnable timeoutCallback;

    private void checkLength() {
        if (items.size() > MAX) {
            throw new UnsupportedOperationException(getClass().getSimpleName() + " only supports up to " + MAX + " items!");
        }
    }

    public void display() {
        checkLength();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("Please pick one of the following options by selecting a reaction below");
        for (int i = 0; i < items.size(); i++) {
            embedBuilder.addField(i + ") " + fieldNameRenderer.apply(items.get(i)),
                    fieldValueRenderer.apply(items.get(i)), true);
        }
        MessageEmbed embed = embedBuilder.build();
        context.getChannel().sendMessage(embed).queue(this::decorate);
    }

    private void decorate(Message message) {
        for (int i = 0; i < items.size(); i++) {
            message.addReaction(EMOTE_NUMBER[i]).queue();
        }
        context.getEventDispatcher().subscribe(MessageReactionAddEvent.class, event ->
                        message.getIdLong() == event.getMessageIdLong()
                                && context.getAuthor().getIdLong() == event.getUser().getIdLong(),
                event -> handleReaction(message, event), true,
                1, TimeUnit.MINUTES, () -> expire(message), false
        );
    }

    private void handleReaction(Message message, MessageReactionAddEvent event) {
        for (int i = 0; i < items.size(); i++) {
            if (EMOTE_NUMBER[i].equals(event.getReactionEmote().getName())) {
                message.delete().queue();
                callback.accept(items.get(i));
                return;
            }
        }
        event.getReaction().removeReaction(event.getUser()).queue();
    }

    private void expire(Message message) {
        message.delete().queue();
        timeoutCallback.run();
    }

}
