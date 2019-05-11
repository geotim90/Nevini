package de.nevini.resolvers;

import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.util.Cleaner;
import de.nevini.util.Picker;
import lombok.NonNull;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractResolver<T> {

    private final String typeName;
    private final Pattern[] optionPatterns;

    protected AbstractResolver(@NonNull String typeName, @NonNull Pattern[] optionPatterns) {
        this.typeName = typeName;
        this.optionPatterns = optionPatterns;
    }

    protected abstract List<T> findSorted(@NonNull CommandEvent event, String query);

    private String getFromOptions(@NonNull CommandEvent event) {
        for (String option : event.getOptions().getOptions()) {
            for (Pattern pattern : optionPatterns) {
                Matcher matcher = pattern.matcher(option);
                if (matcher.matches()) return StringUtils.defaultString(matcher.group(1), StringUtils.EMPTY);
            }
        }
        return null;
    }

    public void resolveArgumentOrOptionOrDefault(@NonNull CommandEvent event, T defaultValue, @NonNull Consumer<T> callback) {
        String query = StringUtils.defaultIfEmpty(event.getArgument(), getFromOptions(event));
        if (StringUtils.isEmpty(query)) {
            callback.accept(defaultValue);
        } else {
            resolveInput(event, query, callback);
        }
    }

    public void resolveArgumentOrOptionOrDefaultIfExists(@NonNull CommandEvent event, T defaultValue, @NonNull Consumer<T> callback) {
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
        if (StringUtils.isEmpty(query)) {
            event.reply("Please enter a " + typeName + " below:", message -> waitForInput(event, message, callback));
        } else {
            resolveInput(event, query, callback);
        }
    }

    public void resolveArgumentOrOptionOrInputIfExists(@NonNull CommandEvent event, @NonNull Consumer<T> callback) {
        String query = StringUtils.defaultIfEmpty(event.getArgument(), getFromOptions(event));
        if (query == null) {
            callback.accept(null);
        } else if (query.isEmpty()) {
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

    public void resolveOptionOrDefaultIfExists(@NonNull CommandEvent event, T defaultValue, @NonNull Consumer<T> callback) {
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
        if (StringUtils.isEmpty(query)) {
            event.reply("Please enter a " + typeName + " below:", message -> waitForInput(event, message, callback));
        } else {
            resolveInput(event, query, callback);
        }
    }

    public void resolveOptionOrInputIfExists(@NonNull CommandEvent event, @NonNull Consumer<T> callback) {
        String query = getFromOptions(event);
        if (query == null) {
            callback.accept(null);
        } else if (query.isEmpty()) {
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
                    if (event.getPrefixService().extractPrefix(e).isPresent()) {
                        replyCancelled(event);
                    } else {
                        resolveInput(event, e.getMessage().getContentRaw(), callback);
                    }
                    Cleaner.tryDelete(message);
                    Cleaner.tryDelete(e.getMessage());
                },
                true,
                1,
                TimeUnit.MINUTES,
                () -> {
                    replyExpired(event);
                    Cleaner.tryDelete(message);
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
            } else if (results.size() > Picker.MAX) {
                replyAmbiguous(event);
            } else if (results.size() > 1) {
                new Picker<>(event, results, this::getFieldNameForPicker, this::getFieldValueForPicker, callback,
                        () -> replyExpired(event)).display();
            } else {
                callback.accept(results.get(0));
            }
        }
    }

    @NonNull
    protected abstract String getFieldNameForPicker(T item);

    @NonNull
    protected abstract String getFieldValueForPicker(T item);

    public void resolveListArgumentOrOptionOrDefault(@NonNull CommandEvent event, List<T> defaultList, @NonNull Consumer<List<T>> callback) {
        String query = StringUtils.defaultIfEmpty(event.getArgument(), getFromOptions(event));
        if (StringUtils.isEmpty(query)) {
            callback.accept(defaultList);
        } else {
            resolveListInput(event, query, callback);
        }
    }

    public void resolveListArgumentOrOptionOrDefaultIfExists(@NonNull CommandEvent event, List<T> defaultList, @NonNull Consumer<List<T>> callback) {
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
        if (StringUtils.isEmpty(query)) {
            event.reply("Please enter a " + typeName + " below:", message -> waitForListInput(event, message, callback));
        } else {
            resolveListInput(event, query, callback);
        }
    }

    public void resolveListArgumentOrOptionOrInputIfExists(@NonNull CommandEvent event, @NonNull Consumer<List<T>> callback) {
        String query = StringUtils.defaultIfEmpty(event.getArgument(), getFromOptions(event));
        if (query == null) {
            callback.accept(null);
        } else if (query.isEmpty()) {
            event.reply("Please enter a " + typeName + " below:", message -> waitForListInput(event, message, callback));
        } else {
            resolveListInput(event, query, callback);
        }
    }

    private void waitForListInput(CommandEvent event, Message message, Consumer<List<T>> callback) {
        event.getEventDispatcher().subscribe(MessageReceivedEvent.class,
                e -> e.getChannel().getId().equals(event.getChannel().getId())
                        && e.getAuthor().getId().equals(event.getAuthor().getId()),
                e -> {
                    if (event.getPrefixService().extractPrefix(e).isPresent()) {
                        replyCancelled(event);
                    } else {
                        resolveListInput(event, e.getMessage().getContentRaw(), callback);
                    }
                    Cleaner.tryDelete(message);
                    Cleaner.tryDelete(e.getMessage());
                },
                true,
                1,
                TimeUnit.MINUTES,
                () -> {
                    replyExpired(event);
                    Cleaner.tryDelete(message);
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
        event.reply(CommandReaction.WARNING, "Too many " + typeName + "s matched your input! Please be more specific next time.", event::complete);
    }

    private void replyCancelled(CommandEvent event) {
        event.reply(event.getAuthor().getAsMention() + ", your previous command was cancelled.", event::complete);
    }

    private void replyExpired(CommandEvent event) {
        event.reply(event.getAuthor().getAsMention() + ", your previous command expired.", event::complete);
    }

    private void replyMissing(CommandEvent event) {
        event.reply(CommandReaction.WARNING, "You did not provide a " + typeName + "!", event::complete);
    }

    private void replyUnknown(CommandEvent event) {
        event.reply(CommandReaction.WARNING, "I could not find any " + typeName + " that matched your input!", event::complete);
    }

}
