package de.nevini.resolvers.common;

import de.nevini.command.CommandEvent;
import de.nevini.resolvers.OptionResolver;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimestampResolver extends OptionResolver<OffsetDateTime> {

    private static final LocalDateTime SAMPLE = LocalDateTime.of(1999, 12, 31, 23, 59, 59, 999000000);

    TimestampResolver() {
        super("timestamp", new Pattern[]{Pattern.compile("(?i)(?:(?:--|//)time(?:stamp)|[-/]t)(?:\\s+(.+))?")});
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax(argument ? "[--time] <timestamp>" : "--time <timestamp>")
                .description("A valid ISO 8601 UTC timestamp (e.g. `" + SAMPLE.toString() + "`)."
                        + "\n`now` can be used as a shortcut for the current date and time."
                        + "\nRelative input such as `5 days ago` or `-24h` can also be used."
                        + (argument ? "\nThe `--time` flag is optional if this option is provided first." : ""))
                .keyword("--time")
                .aliases(new String[]{"//time", "--timestamp", "//timestamp", "-t", "/t"})
                .build();
    }

    @Override
    public List<OffsetDateTime> findSorted(@NonNull CommandEvent ignore, String query) {
        return Optional.ofNullable(parse(query)).map(Collections::singletonList).orElse(Collections.emptyList());
    }

    private OffsetDateTime parse(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }

        if (s.matches("\\d{4}-\\d{2}-\\d{2}.*")) {
            String padding = "1970-01-01T00:00:00";
            String padded = s + padding.substring(Math.min(padding.length(), s.length()));
            try {
                return LocalDateTime.parse(padded).atOffset(ZoneOffset.UTC);
            } catch (DateTimeParseException ignore) {
                return null;
            }
        }

        switch (s.toLowerCase()) {
            case "now":
            case "just now":
            case "today":
                return OffsetDateTime.now(ZoneOffset.UTC);
            case "yesterday":
                return OffsetDateTime.now(ZoneOffset.UTC).minusDays(1);
            case "tomorrow":
                return OffsetDateTime.now(ZoneOffset.UTC).plusDays(1);
            default:
                break; // ignore
        }

        Matcher matcher = Pattern.compile("(\\d+) ?d(ays?)? ago").matcher(s);
        if (matcher.matches()) {
            return OffsetDateTime.now(ZoneOffset.UTC).minusDays(Long.parseLong(matcher.group(1)));
        }

        matcher = Pattern.compile("(-?\\d+) ?d(ays?)?").matcher(s);
        if (matcher.matches()) {
            return OffsetDateTime.now(ZoneOffset.UTC).plusDays(Long.parseLong(matcher.group(1)));
        }

        matcher = Pattern.compile("(\\d+) ?h(ours?)? ago").matcher(s);
        if (matcher.matches()) {
            return OffsetDateTime.now(ZoneOffset.UTC).minusHours(Long.parseLong(matcher.group(1)));
        }

        matcher = Pattern.compile("(-?\\d+) ?h(ours?)?").matcher(s);
        if (matcher.matches()) {
            return OffsetDateTime.now(ZoneOffset.UTC).plusHours(Long.parseLong(matcher.group(1)));
        }

        // no match
        return null;
    }

    @Override
    protected @NonNull String getFieldNameForPicker(OffsetDateTime item) {
        return Formatter.formatTimestamp(item);
    }

    @Override
    protected @NonNull String getFieldValueForPicker(OffsetDateTime item) {
        return Long.toString(item.toInstant().toEpochMilli());
    }

}
