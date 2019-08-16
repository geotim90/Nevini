package de.nevini.resolvers;

import de.nevini.command.CommandEvent;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import de.nevini.util.message.MessageCleaner;
import de.nevini.util.message.PickerEmbed;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public abstract class OptionResolver<T> extends FlagResolver {

    private final String typeName;

    protected OptionResolver(@NonNull String typeName, @NonNull Pattern[] optionPatterns) {
        super(optionPatterns);
        this.typeName = typeName;
    }

    @Override
    public CommandOptionDescriptor describe() {
        return describe(false, false);
    }

    public abstract CommandOptionDescriptor describe(boolean list, boolean argument);

    public abstract List<T> findSorted(@NonNull CommandEvent event, String query);

    public void resolveArgumentOrOptionOrDefault(
            @NonNull CommandEvent event, T defaultValue, @NonNull Consumer<T> callback
    ) {
        String query = StringUtils.defaultIfEmpty(event.getArgument(), getFromOptions(event));
        if (StringUtils.isEmpty(query)) {
            callback.accept(defaultValue);
        } else {
            resolveInput(event, query, callback);
        }
    }

    public void resolveArgumentOrOptionOrDefaultIfExists(
            @NonNull CommandEvent event, T defaultValue, @NonNull Consumer<T> callback
    ) {
        String query = StringUtils.defaultIfEmpty(event.getArgument(), getFromOptions(event));
        if (query == null) {
            callback.accept(null);
        } else if (query.isEmpty()) {
            callback.accept(defaultValue);
        } else {
            resolveInput(event, query, callback);
        }
    }

    public void resolveArgumentOrOptionOrInput(@NonNull CommandEvent event, @NonNull Consumer<T> callback) {
        String query = StringUtils.defaultIfEmpty(event.getArgument(), getFromOptions(event));
        if (StringUtils.isEmpty(query) && !event.isDm()) {
            event.reply("Please enter a " + typeName + " below:", message -> waitForInput(event, message, callback));
        } else {
            resolveInput(event, query, callback);
        }
    }

    public void resolveArgumentOrOptionOrInputIfExists(@NonNull CommandEvent event, @NonNull Consumer<T> callback) {
        String query = StringUtils.defaultIfEmpty(event.getArgument(), getFromOptions(event));
        if (query == null) {
            callback.accept(null);
        } else if (query.isEmpty() && !event.isDm()) {
            event.reply("Please enter a " + typeName + " below:", message -> waitForInput(event, message, callback));
        } else {
            resolveInput(event, query, callback);
        }
    }

    public void resolveOptionOrDefault(@NonNull CommandEvent event, T defaultValue, @NonNull Consumer<T> callback) {
        String query = getFromOptions(event);
        if (StringUtils.isEmpty(query)) {
            callback.accept(defaultValue);
        } else {
            resolveInput(event, query, callback);
        }
    }

    public void resolveOptionOrDefaultIfExists(
            @NonNull CommandEvent event, T defaultValue, @NonNull Consumer<T> callback
    ) {
        String query = getFromOptions(event);
        if (query == null) {
            callback.accept(null);
        } else if (query.isEmpty()) {
            callback.accept(defaultValue);
        } else {
            resolveInput(event, query, callback);
        }
    }

    public void resolveOptionOrInput(@NonNull CommandEvent event, @NonNull Consumer<T> callback) {
        String query = getFromOptions(event);
        if (StringUtils.isEmpty(query) && !event.isDm()) {
            event.reply("Please enter a " + typeName + " below:", message -> waitForInput(event, message, callback));
        } else {
            resolveInput(event, query, callback);
        }
    }

    public void resolveOptionOrInputIfExists(@NonNull CommandEvent event, @NonNull Consumer<T> callback) {
        String query = getFromOptions(event);
        if (query == null) {
            callback.accept(null);
        } else if (query.isEmpty() && !event.isDm()) {
            event.reply("Please enter a " + typeName + " below:", message -> waitForInput(event, message, callback));
        } else {
            resolveInput(event, query, callback);
        }
    }

    private void waitForInput(CommandEvent event, Message message, Consumer<T> callback) {
        event.getEventDispatcher().subscribe(MessageReceivedEvent.class,
                e -> e.getChannel().getId().equals(event.getChannel().getId())
                        && e.getAuthor().getId().equals(event.getAuthor().getId()),
                e -> {
                    if (StringUtils.isNotEmpty(event.getPrefixService().extractPrefix(e))) {
                        replyCancelled(event);
                    } else {
                        resolveInput(event, e.getMessage().getContentRaw(), callback);
                        MessageCleaner.tryDelete(e.getMessage());
                    }
                    MessageCleaner.tryDelete(message);
                },
                true,
                1,
                TimeUnit.MINUTES,
                () -> {
                    replyExpired(event);
                    MessageCleaner.tryDelete(message);
                },
                false
        );
    }

    private void resolveInput(CommandEvent event, String input, Consumer<T> callback) {
        if (StringUtils.isEmpty(input)) {
            replyMissing(event);
        } else {
            List<T> results = findSorted(event, input);
            if (results.isEmpty()) {
                replyUnknown(event);
            } else if (results.size() > PickerEmbed.MAX) {
                replyAmbiguous(event);
            } else if (results.size() > 1) {
                new PickerEmbed<>(
                        event.getChannel(), event.getAuthor(), event.createEmbedBuilder(),
                        results, this::getFieldNameForPicker, this::getFieldValueForPicker,
                        event.getEventDispatcher(), callback, () -> replyExpired(event)
                ).display();
            } else {
                callback.accept(results.get(0));
            }
        }
    }

    @NonNull
    protected abstract String getFieldNameForPicker(T item);

    @NonNull
    protected abstract String getFieldValueForPicker(T item);

    public void resolveListArgumentOrOptionOrDefault(
            @NonNull CommandEvent event, List<T> defaultList, @NonNull Consumer<List<T>> callback
    ) {
        String query = StringUtils.defaultIfEmpty(event.getArgument(), getFromOptions(event));
        if (StringUtils.isEmpty(query)) {
            callback.accept(defaultList);
        } else {
            resolveListInput(event, query, callback);
        }
    }

    public void resolveListArgumentOrOptionOrDefaultIfExists(
            @NonNull CommandEvent event, List<T> defaultList, @NonNull Consumer<List<T>> callback
    ) {
        String query = StringUtils.defaultIfEmpty(event.getArgument(), getFromOptions(event));
        if (query == null) {
            callback.accept(Collections.emptyList());
        } else if (query.isEmpty()) {
            callback.accept(defaultList);
        } else {
            resolveListInput(event, query, callback);
        }
    }

    public void resolveListArgumentOrOptionOrInput(@NonNull CommandEvent event, @NonNull Consumer<List<T>> callback) {
        String query = StringUtils.defaultIfEmpty(event.getArgument(), getFromOptions(event));
        if (StringUtils.isEmpty(query) && !event.isDm()) {
            event.reply(
                    "Please enter a " + typeName + " below:",
                    message -> waitForListInput(event, message, callback)
            );
        } else {
            resolveListInput(event, query, callback);
        }
    }

    public void resolveListArgumentOrOptionOrInputIfExists(
            @NonNull CommandEvent event, @NonNull Consumer<List<T>> callback
    ) {
        String query = StringUtils.defaultIfEmpty(event.getArgument(), getFromOptions(event));
        if (query == null) {
            callback.accept(null);
        } else if (query.isEmpty() && !event.isDm()) {
            event.reply("Please enter a " + typeName + " below:",
                    message -> waitForListInput(event, message, callback));
        } else {
            resolveListInput(event, query, callback);
        }
    }

    private void waitForListInput(CommandEvent event, Message message, Consumer<List<T>> callback) {
        event.getEventDispatcher().subscribe(MessageReceivedEvent.class,
                e -> e.getChannel().getId().equals(event.getChannel().getId())
                        && e.getAuthor().getId().equals(event.getAuthor().getId()),
                e -> {
                    if (event.getPrefixService().extractPrefix(e) != null) {
                        replyCancelled(event);
                    } else {
                        resolveListInput(event, e.getMessage().getContentRaw(), callback);
                    }
                    MessageCleaner.tryDelete(message);
                    MessageCleaner.tryDelete(e.getMessage());
                },
                true,
                1,
                TimeUnit.MINUTES,
                () -> {
                    replyExpired(event);
                    MessageCleaner.tryDelete(message);
                },
                false
        );
    }

    private void resolveListInput(CommandEvent event, String input, Consumer<List<T>> callback) {
        if (StringUtils.isEmpty(input)) {
            replyMissing(event);
        } else {
            List<T> results = findSorted(event, input);
            if (results.isEmpty()) {
                replyUnknown(event);
            } else {
                callback.accept(results);
            }
        }
    }

    private void replyAmbiguous(CommandEvent event) {
        event.reply(CommandReaction.WARNING, "Too many " + typeName
                + "s matched your input! Please be more specific next time.", event::complete);
    }

    private void replyCancelled(CommandEvent event) {
        event.reply(CommandReaction.DEFAULT_NOK, event::complete);
    }

    private void replyExpired(CommandEvent event) {
        event.reply(CommandReaction.DEFAULT_NOK, event::complete);
    }

    private void replyMissing(CommandEvent event) {
        event.reply(CommandReaction.WARNING, "You did not provide a " + typeName + "!", event::complete);
    }

    private void replyUnknown(CommandEvent event) {
        event.reply(CommandReaction.WARNING, "I could not find any " + typeName + " that matched your input!",
                event::complete);
    }

}
