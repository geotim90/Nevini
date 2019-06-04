package de.nevini.bot.command;

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

@Value
public class CommandOptions {

    public static CommandOptions parseArgument(String argument) {
        if (StringUtils.isEmpty(argument)) {
            return new CommandOptions(Collections.emptyList());
        } else {
            return new CommandOptions(Arrays.asList(
                    argument.split("\\s+(?=" + CommandPatterns.OPTION + "|" + CommandPatterns.MENTION + ")")
            ));
        }
    }

    @NonNull
    private final List<String> arguments;

    private final Lazy<String> argument = Lazy.of(this::supplyArgument);
    private final Lazy<List<String>> options = Lazy.of(this::supplyOptions);

    public Optional<String> getArgument() {
        return argument.getOptional();
    }

    private String supplyArgument() {
        Optional<String> argument = getArguments().stream().findFirst();
        if (argument.isPresent()
                && (argument.get().matches(CommandPatterns.OPTION) || argument.get().matches(CommandPatterns.MENTION))
        ) {
            return null;
        } else {
            return argument.orElse(null);
        }
    }

    public CommandOptions withArgument(String argument) {
        if (StringUtils.isEmpty(argument)) {
            return new CommandOptions(getOptions());
        } else {
            return new CommandOptions(ListUtils.union(Collections.singletonList(argument), getOptions()));
        }
    }

    public List<String> getOptions() {
        return options.get();
    }

    private List<String> supplyOptions() {
        return Collections.unmodifiableList(getArguments()
                .stream()
                .filter(argument ->
                        argument.matches(CommandPatterns.OPTION) || argument.matches(CommandPatterns.MENTION))
                .collect(Collectors.toList()));
    }

}
