package de.nevini.resolvers.external;

import com.oopsjpeg.osu4j.GameMode;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.AbstractResolver;
import de.nevini.util.Finder;

import java.util.List;
import java.util.regex.Pattern;

public class OsuModeResolver extends AbstractResolver<GameMode> {

    public OsuModeResolver() {
        super("mode", new Pattern[]{Pattern.compile("(?i)(?:--|//)mode(?:\\s+(.+))?")});
    }

    @Override
    public CommandOptionDescriptor describe(boolean resolvesArgument, boolean resolvesList) {
        return CommandOptionDescriptor.builder()
                .syntax(resolvesArgument ? "[--mode] <mode>" : "--mode <mode>")
                .description("Refers to " + (resolvesList ? "all osu! game modes" : "an osu! game mode")
                        + " with a matching name (`osu!`, `osu!taiko`, `osu!catch` or `osu!mania`)."
                        + (resolvesArgument ? "\nThe `--mode` flag is optional if this option is provided first." : ""))
                .keyword("--mode")
                .aliases(new String[]{"//mode"})
                .build();
    }

    @Override
    public List<GameMode> findSorted(CommandEvent ignore, String query) {
        return Finder.findAny(GameMode.values(), mode -> new String[]{mode.getName(), mode.name(), mode.name().replace('_', ' ')}, query);
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
