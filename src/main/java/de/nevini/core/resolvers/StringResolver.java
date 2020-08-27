package de.nevini.core.resolvers;

import de.nevini.core.command.CommandEvent;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class StringResolver extends OptionResolver<String> {

    private static Pattern compileOptionPattern(String optionName) {
        return Pattern.compile("(?i)(?:--|//)" + Pattern.quote(optionName) + "(?:\\s+(.+))?");
    }

    private static Pattern compileOptionPattern(String optionName, String flagName) {
        return Pattern.compile(
                "(?i)(?:(?:--|//)" + Pattern.quote(optionName) + "|[-/]" + Pattern.quote(flagName) + ")(?:\\s+(.+))?"
        );
    }

    private final CommandOptionDescriptor descriptor;

    public StringResolver(
            @NonNull String typeName, @NonNull String optionName, @NonNull CommandOptionDescriptor descriptor
    ) {
        super(typeName, new Pattern[]{compileOptionPattern(optionName)});
        this.descriptor = descriptor;
    }

    public StringResolver(@NonNull String typeName, @NonNull String optionName, @NonNull String flagName,
                          @NonNull CommandOptionDescriptor descriptor
    ) {
        super(typeName, new Pattern[]{compileOptionPattern(optionName, flagName)});
        this.descriptor = descriptor;
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return descriptor;
    }

    @Override
    public List<String> findSorted(@NonNull CommandEvent event, String query) {
        return StringUtils.isEmpty(query) ? Collections.emptyList() : Collections.singletonList(query);
    }

    @Override
    protected @NonNull String getFieldNameForPicker(String item) {
        return item;
    }

    @Override
    protected @NonNull String getFieldValueForPicker(String item) {
        return "";
    }

}
