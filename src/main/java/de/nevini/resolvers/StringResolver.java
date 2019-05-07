package de.nevini.resolvers;

import de.nevini.command.CommandEvent;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class StringResolver extends AbstractResolver<String> {

    private static Pattern compileOptionPattern(String optionName) {
        return Pattern.compile("(?i)(?:--|//)" + Pattern.quote(optionName) + "(?:\\s+(.+))");
    }

    public StringResolver(@NonNull String typeName) {
        super(typeName, new Pattern[0]);
    }

    public StringResolver(@NonNull String typeName, @NonNull String optionName) {
        super(typeName, new Pattern[]{compileOptionPattern(optionName)});
    }

    @Override
    protected List<String> findSorted(@NonNull CommandEvent event, String query) {
        return StringUtils.isEmpty(query) ? Collections.emptyList() : Collections.singletonList(query);
    }

    @Override
    protected String getFieldNameForPicker(String item) {
        return item;
    }

    @Override
    protected String getFieldValueForPicker(String item) {
        return null;
    }

}
