package de.nevini.bot.resolvers;

import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.command.CommandOptionDescriptor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class StringResolver extends AbstractResolver<String> {

    private static Pattern compileOptionPattern(String optionName) {
        return Pattern.compile("(?i)(?:--|//)" + Pattern.quote(optionName) + "(?:\\s+(.+))?");
    }

    private final CommandOptionDescriptor descriptor;

    public StringResolver(
            @NonNull String typeName, @NonNull String optionName, @NonNull CommandOptionDescriptor descriptor
    ) {
        super(typeName, new Pattern[]{compileOptionPattern(optionName)});
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
    protected String getFieldNameForPicker(String item) {
        return item;
    }

    @Override
    protected String getFieldValueForPicker(String item) {
        return "";
    }

}
