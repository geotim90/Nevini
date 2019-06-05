package de.nevini.bot.resolvers.external;

import com.oopsjpeg.osu4j.GameMode;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.resolvers.AbstractResolver;
import de.nevini.commons.util.Finder;
import de.nevini.framework.command.CommandOptionDescriptor;

import java.util.List;
import java.util.regex.Pattern;

public class OsuModeResolver extends AbstractResolver<GameMode> {

    protected OsuModeResolver() {
        super("mode", new Pattern[]{Pattern.compile("(?i)(?:--|//)mode(?:\\s+(.+))?")});
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax(argument ? "[--mode] <mode>" : "--mode <mode>")
                .description("Refers to " + (list ? "all osu! game modes" : "an osu! game mode")
                        + " with a matching name (`osu!`, `osu!taiko`, `osu!catch` or `osu!mania`)."
                        + (argument ? "\nThe `--mode` flag is optional if this option is provided first." : ""))
                .keyword("--mode")
                .aliases(new String[]{"//mode"})
                .build();
    }

    @Override
    public List<GameMode> findSorted(CommandEvent ignore, String query) {
        return Finder.findAny(GameMode.values(), mode -> new String[]{mode.getName(), mode.name(),
                mode.name().replace('_', ' ')}, query);
    }

    @Override
    protected String getFieldNameForPicker(GameMode item) {
        return item.getName();
    }

    @Override
    protected String getFieldValueForPicker(GameMode item) {
        return "";
    }

}
