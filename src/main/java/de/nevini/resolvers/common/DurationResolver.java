package de.nevini.resolvers.common;

import de.nevini.command.CommandEvent;
import de.nevini.resolvers.OptionResolver;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationResolver extends OptionResolver<Long> {

    DurationResolver() {
        super("duration", new Pattern[]{Pattern.compile("(?i)(?:(?:--|//)(?:duration|days)|[-/]d)(?:\\s+(.+))?")});
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax(argument ? "[--duration] <days>" : "--duration <days>")
                .description("A number of days."
                        + "\nInput such as `5 days` or `2w` can also be used."
                        + (argument ? "\nThe `--duration` flag is optional if this option is provided first." : ""))
                .keyword("--duration")
                .aliases(new String[]{"//duration", "--days", "//days", "-d", "/d"})
                .build();
    }

    @Override
    public List<Long> findSorted(@NonNull CommandEvent ignore, String query) {
        return Optional.ofNullable(parse(query)).map(Collections::singletonList).orElse(Collections.emptyList());
    }

    private Long parse(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }

        if (s.matches("\\d+")) {
            return parsePositiveLong(s, 1);
        }

        Matcher matcher = Pattern.compile("(\\d+) ?d(?:ays?)? ago").matcher(s);
        if (matcher.matches()) {
            return parsePositiveLong(matcher.group(1), 1);
        }

        matcher = Pattern.compile("(\\d+) ?w(?:eeks?)?").matcher(s);
        if (matcher.matches()) {
            return parsePositiveLong(matcher.group(1), 7);
        }

        matcher = Pattern.compile("(\\d+) ?m(?:onths?)?").matcher(s);
        if (matcher.matches()) {
            return parsePositiveLong(matcher.group(1), 30);
        }

        matcher = Pattern.compile("(\\d+) ?y(?:ears?)?").matcher(s);
        if (matcher.matches()) {
            return parsePositiveLong(matcher.group(1), 365);
        }

        // no match
        return null;
    }

    private static Long parsePositiveLong(String value, int factor) {
        if (StringUtils.isNotEmpty(value)) {
            try {
                long result = Long.parseLong(value);
                if (result > 0) return result * factor;
            } catch (NumberFormatException ignore) {
            }
        }
        return null;
    }

    @Override
    protected @NonNull String getFieldNameForPicker(Long item) {
        return Formatter.formatInteger(item);
    }

    @Override
    protected @NonNull String getFieldValueForPicker(Long item) {
        return StringUtils.EMPTY;
    }

}
