package de.nevini.modules.osu.resolvers;

import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.OptionResolver;
import de.nevini.modules.osu.model.OsuMode;
import de.nevini.util.Finder;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;

import java.util.List;
import java.util.regex.Pattern;

public class OsuModeResolver extends OptionResolver<OsuMode> {

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
    public List<OsuMode> findSorted(@NonNull CommandEvent ignore, String query) {
        return Finder.findAnyLenient(OsuMode.values(), mode -> new String[]{
                Integer.toString(mode.getId()),
                mode.getName(),
                mode.name(),
                mode.name().replace('_', ' ')
        }, query);
    }

    @Override
    protected @NonNull String getFieldNameForPicker(OsuMode item) {
        return item.getName();
    }

    @Override
    protected @NonNull String getFieldValueForPicker(OsuMode item) {
        return "";
    }

}
