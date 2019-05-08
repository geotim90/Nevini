package de.nevini.resolvers;

import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.util.Picker;
import lombok.NonNull;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

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
                if (matcher.matches()) return matcher.group(1);
            }
        }
        return null;
    }

    public void resolveArgumentOrOptionIfExists(@NonNull CommandEvent event, @NonNull Consumer<T> callback) {
        String query = StringUtils.defaultString(event.getArgument(), getFromOptions(event));
        if (StringUtils.isEmpty(query)) {
            callback.accept(null);
        } else {
            resolveInput(event, query, callback);
        }
    }

    public void resolveArgumentOrOptionOrDefault(@NonNull CommandEvent event, T defaultValue, @NonNull Consumer<T> callback) {
        String query = StringUtils.defaultString(event.getArgument(), getFromOptions(event));
        if (StringUtils.isEmpty(query)) {
            callback.accept(defaultValue);
        } else {
            resolveInput(event, query, callback);
        }
    }

    public void resolveArgumentOrOptionOrInput(@NonNull CommandEvent event, @NonNull Consumer<T> callback) {
        String query = StringUtils.defaultIfEmpty(event.getArgument(), getFromOptions(event));
        if (StringUtils.isEmpty(query)) {
            if (event.canTalk()) {
                event.reply("Please enter a " + typeName + " below:", message -> waitForInput(event, callback));
            } else {
                replyMissing(event);
            }
        } else {
            resolveInput(event, query, callback);
        }
    }

    public void resolveOptionIfExists(@NonNull CommandEvent event, @NonNull Consumer<T> callback) {
        resolveOptionIfExists(event, event.getMessage(), callback);
    }

    public void resolveOptionIfExists(@NonNull CommandEvent event, @NonNull Message message,
                                      @NonNull Consumer<T> callback) {
        String query = getFromOptions(event);
        if (StringUtils.isEmpty(query)) {
            callback.accept(null);
        } else {
            resolveInput(event, query, callback);
        }
    }

    private void waitForInput(CommandEvent event, Consumer<T> callback) {
        event.getEventDispatcher().subscribe(MessageReceivedEvent.class,
                e -> e.getChannel().getId().equals(event.getChannel().getId())
                        && e.getAuthor().getId().equals(event.getAuthor().getId()),
                e -> {
                    if (event.getPrefixService().extractPrefix(e).isPresent()) {
                        replyCancelled(event);
                    } else {
                        resolveInput(event, e.getMessage().getContentRaw(), callback);
                    }
                },
                true,
                1,
                TimeUnit.MINUTES,
                () -> replyExpired(event),
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
                        () -> replyCancelled(event)).display();
            } else {
                callback.accept(results.get(0));
            }
        }
    }

    protected abstract String getFieldNameForPicker(T item);

    protected abstract String getFieldValueForPicker(T item);

    private void replyAmbiguous(CommandEvent event) {
        event.reply(CommandReaction.WARNING, "Too many " + typeName + "s matched your input! Please be more specific next time.");
    }

    private void replyCancelled(CommandEvent event) {
        event.reply(event.getAuthor().getAsMention() + ", your previous command was cancelled.");
    }

    private void replyExpired(CommandEvent event) {
        event.reply(event.getAuthor().getAsMention() + ", your previous command expired.");
    }

    private void replyMissing(CommandEvent event) {
        event.reply(CommandReaction.WARNING, "You did not provide a " + typeName + "!");
    }

    private void replyUnknown(CommandEvent event) {
        event.reply(CommandReaction.WARNING, "I could not find any " + typeName + " that matched your input!");
    }

}
