package de.nevini.core.resolvers.common;

import de.nevini.core.resolvers.FlagResolver;
import de.nevini.util.command.CommandOptionDescriptor;

import java.util.regex.Pattern;

public class GuildFlagResolver extends FlagResolver {

    GuildFlagResolver() {
        super(new Pattern[]{
                Pattern.compile("(?i)(?:(?:--|//)(?:server|guild)|[-/][sg])(?:\\s+(.+))?")
        });
    }

    @Override
    public CommandOptionDescriptor describe() {
        return CommandOptionDescriptor.builder()
                .syntax("--server")
                .description("Refers to the current server.")
                .keyword("--server")
                .aliases(new String[]{"//server", "--guild", "//guild", "-s", "/s", "-g", "/g"})
                .build();
    }

}
