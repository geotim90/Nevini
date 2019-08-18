package de.nevini.resolvers;

import de.nevini.command.CommandEvent;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class FlagResolver {

    private final Pattern[] optionPatterns;

    protected FlagResolver(@NonNull Pattern[] optionPatterns) {
        this.optionPatterns = optionPatterns;
    }

    public abstract CommandOptionDescriptor describe();

    String getFromOptions(@NonNull CommandEvent event) {
        for (String option : event.getOptions().getOptions()) {
            for (Pattern pattern : optionPatterns) {
                Matcher matcher = pattern.matcher(option);
                if (matcher.matches()) {
                    if (matcher.groupCount() > 0) {
                        return StringUtils.defaultString(matcher.group(1), StringUtils.EMPTY);
                    } else {
                        return StringUtils.EMPTY;
                    }
                }
            }
        }
        return null;
    }

    public boolean isOptionPresent(@NonNull CommandEvent event) {
        return getFromOptions(event) != null;
    }

}
