package de.nevini.resolvers.external;

import com.oopsjpeg.osu4j.GameMod;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.AbstractResolver;
import de.nevini.util.Finder;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OsuModsResolver extends AbstractResolver<GameMod[]> {

    private static final Map<String, GameMod> MODS;

    static {
        Map<String, GameMod> mods = new LinkedHashMap<>();
        mods.put("EZ", GameMod.EASY);
        mods.put("NF", GameMod.NO_FAIL);
        mods.put("HT", GameMod.HALF_TIME);
        mods.put("HR", GameMod.HARD_ROCK);
        mods.put("SD", GameMod.SUDDEN_DEATH);
        mods.put("PF", GameMod.PERFECT);
        mods.put("DT", GameMod.DOUBLE_TIME);
        mods.put("NC", GameMod.NIGHTCORE);
        mods.put("HD", GameMod.HIDDEN);
        mods.put("FL", GameMod.FLASHLIGHT);
        mods.put("SO", GameMod.SPUNOUT);
        mods.put("TD", GameMod.TOUCH_DEVICE);
        MODS = Collections.unmodifiableMap(mods);
    }

    public static CommandOptionDescriptor.CommandOptionDescriptorBuilder describe() {
        return CommandOptionDescriptor.builder()
                .syntax("--mods <mods>")
                .description("Refers to osu! mods using a name (e.g. \"No Fail\") or short codes (e.g. `NF`, `HDHR`, `HDHRNCFL`)")
                .keyword("--mods")
                .aliases(new String[]{"//mods", "--mod", "//mod"});
    }

    public OsuModsResolver() {
        super("mods", new Pattern[]{Pattern.compile("(?i)(?:--|//)mods?(?:\\s+(.+))?")});
    }

    @Override
    public CommandOptionDescriptor describe(boolean resolvesArgument, boolean resolvesList) {
        return CommandOptionDescriptor.builder()
                .syntax(resolvesArgument ? "[--mods] <mods>" : "--mods <mods>")
                .description("Refers to osu! mods with a matching name (e.g. \"No Fail\") or short code (e.g. `NF`, `HDHR`, `HDHRNCFL`)."
                        + (resolvesArgument ? "\nThe `--mods` flag is optional if this option is provided first." : ""))
                .keyword("--mods")
                .aliases(new String[]{"//mods", "--mod", "//mod"})
                .build();
    }

    @Override
    public List<GameMod[]> findSorted(CommandEvent ignore, String query) {
        if (query.matches("(?i)(EZ|NF|HT|HR|SD|PF|DT|NC|HD|FL|SO|TD)+")) {
            int count = query.length() / 2;
            GameMod[] mods = new GameMod[count];
            for (int i = 0; i < count; ++i) {
                mods[i] = MODS.get(query.substring(i * 2, (i + 1) * 2));
            }
            return Collections.singletonList(mods);
        } else {
            return Finder.findAny(MODS.values(), mod -> new String[]{
                    mod.getName(),
                    mod.name(),
                    mod.name().replace('_', ' ')
            }, query).stream().map(mod -> new GameMod[]{mod}).collect(Collectors.toList());
        }
    }

    @Override
    protected String getFieldNameForPicker(GameMod[] item) {
        // should only be called for ambiguous single mod inputs
        return item[0].getName();
    }

    @Override
    protected String getFieldValueForPicker(GameMod[] item) {
        return "";
    }

}
