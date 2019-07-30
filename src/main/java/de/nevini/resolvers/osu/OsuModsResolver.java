package de.nevini.resolvers.osu;

import de.nevini.api.osu.model.OsuMod;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.AbstractResolver;
import de.nevini.util.Finder;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OsuModsResolver extends AbstractResolver<OsuMod[]> {

    private static final Map<String, OsuMod> MODS;

    static {
        Map<String, OsuMod> mods = new LinkedHashMap<>();
        mods.put("EZ", OsuMod.EASY);
        mods.put("NF", OsuMod.NO_FAIL);
        mods.put("HT", OsuMod.HALF_TIME);
        mods.put("HR", OsuMod.HARD_ROCK);
        mods.put("SD", OsuMod.SUDDEN_DEATH);
        mods.put("PF", OsuMod.PERFECT);
        mods.put("DT", OsuMod.DOUBLE_TIME);
        mods.put("NC", OsuMod.NIGHTCORE);
        mods.put("HD", OsuMod.HIDDEN);
        mods.put("FL", OsuMod.FLASHLIGHT);
        mods.put("SO", OsuMod.SPUNOUT);
        mods.put("TD", OsuMod.TOUCH_DEVICE);
        MODS = Collections.unmodifiableMap(mods);
    }

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
        if (query.matches("(?i)(EZ|NF|HT|HR|SD|PF|DT|NC|HD|FL|SO|TD)+")) {
            int count = query.length() / 2;
            OsuMod[] mods = new OsuMod[count];
            for (int i = 0; i < count; ++i) {
                mods[i] = MODS.get(query.substring(i * 2, (i + 1) * 2));
            }
            return Collections.singletonList(mods);
        } else {
            return Finder.findAny(MODS.values(), mod -> new String[]{
                    mod.getName(),
                    mod.name(),
                    mod.name().replace('_', ' ')
            }, query).stream().map(mod -> new OsuMod[]{mod}).collect(Collectors.toList());
        }
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
