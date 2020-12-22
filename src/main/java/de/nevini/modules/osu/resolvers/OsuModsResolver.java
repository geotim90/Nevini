package de.nevini.modules.osu.resolvers;

import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.OptionResolver;
import de.nevini.modules.osu.model.OsuMod;
import de.nevini.util.Finder;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OsuModsResolver extends OptionResolver<OsuMod[]> {

    protected OsuModsResolver() {
        super("mods", new Pattern[]{Pattern.compile("(?i)(?:--|//)mods?(?:\\s+(.+))?")});
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax(argument ? "[--mods] <mods>" : "--mods <mods>")
                .description("Refers to osu! mods with a matching name (e.g. \"No Fail\") "
                        + "or short code (e.g. `NF`, `HDHR`, `HDHRNCFL`)."
                        + (argument ? "\nThe `--mods` flag is optional if this option is provided first." : ""))
                .keyword("--mods")
                .aliases(new String[]{"//mods", "--mod", "//mod"})
                .build();
    }

    @Override
    public List<OsuMod[]> findSorted(@NonNull CommandEvent ignore, String query) {
        if (query.matches(getModCodesRegex())) {
            int count = query.length() / 2;
            OsuMod[] mods = new OsuMod[count];
            for (int i = 0; i < count; ++i) {
                mods[i] = resolveCode(query.substring(i * 2, (i + 1) * 2));
            }
            return Collections.singletonList(mods);
        } else {
            return Finder.findAny(OsuMod.values(), mod -> new String[]{
                    mod.getCode(),
                    mod.getName(),
                    mod.name(),
                    mod.name().replace('_', ' ')
            }, query).stream().map(mod -> new OsuMod[]{mod}).collect(Collectors.toList());
        }
    }

    private String getModCodesRegex() {
        return "(?i)(" + Arrays.stream(OsuMod.values()).map(OsuMod::getCode).collect(Collectors.joining("|")) + ")+";
    }

    private OsuMod resolveCode(String code) {
        return Arrays.stream(OsuMod.values()).filter(e -> e.getCode().equalsIgnoreCase(code)).findAny().orElse(null);
    }

    @Override
    protected @NonNull String getFieldNameForPicker(OsuMod[] item) {
        // should only be called for ambiguous single mod inputs
        return item[0].getName();
    }

    @Override
    protected @NonNull String getFieldValueForPicker(OsuMod[] item) {
        return "";
    }

}
