package de.nevini.framework.command;

import lombok.NonNull;
import lombok.Value;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.util.Lazy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents the command options, flags, mentions and arguments for when a command is called.
 */
@Value
public class CommandOptions {

    /**
     * Returns a new {@link CommandOptions} instance for the provided {@code argument}.
     * Returns an empty {@link CommandOptions} instance if {@code argument} is {@code null} or empty.
     *
     * @param argument the options, flags, mentions and arguments to be parsed
     */
    public static CommandOptions parseArgument(String argument) {
        if (StringUtils.isEmpty(argument)) {
            return new CommandOptions(Collections.emptyList());
        } else {
            return new CommandOptions(Arrays.asList(
                    argument.split("\\s+(?=" + CommandPatterns.OPTION + "|" + CommandPatterns.MENTION + ")")
            ));
        }
    }

    /**
     * Contains a list of all options, flags, mentions and arguments as raw strings.
     */
    @NonNull
    private final List<String> arguments;

    private final Lazy<String> argument = Lazy.of(this::supplyArgument);
    private final Lazy<List<String>> options = Lazy.of(this::supplyOptions);

    /**
     * Returns the argument (any text after the command keyword and before the first option, flag or mention).
     * Returns an empty {@link Optional} if no argument is available.
     */
    public Optional<String> getArgument() {
        return argument.getOptional();
    }

    /**
     * Extracts the result for {@link #getArgument()} from {@link #arguments}.
     */
    private String supplyArgument() {
        // the argument will always be first
        Optional<String> argument = getArguments().stream().findFirst();
        // check that anything is there and that it is not an option, flag or mention
        if (argument.isPresent()
                && (argument.get().matches(CommandPatterns.OPTION) || argument.get().matches(CommandPatterns.MENTION))
        ) {
            return null;
        } else {
            return argument.orElse(null);
        }
    }

    /**
     * Returns a {@link List} of options, flags and mentions.
     * Returns an empty {@link List} if no such arguments are available.
     */
    public List<String> getOptions() {
        return options.get();
    }


    /**
     * Extract the results for {@link #getOptions()} from {@link #arguments}.
     */
    private List<String> supplyOptions() {
        return Collections.unmodifiableList(getArguments().stream()
                .filter(arg -> arg.matches(CommandPatterns.OPTION) || arg.matches(CommandPatterns.MENTION))
                .collect(Collectors.toList()));
    }

    /**
     * Returns a copy of this {@link CommandOptions} instance with the provided {@code argument} applied to it.
     * This does not modify the {@link CommandOptions} instance the method is called on.
     * The returned value will be a new {@link CommandOptions} instance.
     *
     * @param argument the argument {@link String} to use for the new {@link CommandOptions} instance
     *                 (can also be {@code null} or empty)
     */
    public CommandOptions withArgument(String argument) {
        if (StringUtils.isEmpty(argument)) {
            return new CommandOptions(getOptions());
        } else {
            return new CommandOptions(ListUtils.union(Collections.singletonList(argument), getOptions()));
        }
    }

}
