package de.nevini.resolvers.external;

import com.oopsjpeg.osu4j.GameMode;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.AbstractResolver;
import de.nevini.util.Finder;
import lombok.NonNull;

import java.util.List;
import java.util.regex.Pattern;

public class OsuModeResolver extends AbstractResolver<GameMode> {

    public static CommandOptionDescriptor.CommandOptionDescriptorBuilder describe() {
        return CommandOptionDescriptor.builder()
                .syntax("--mode <mode>")
                .description("Refers to an osu! game mode (osu!, osu!taiko, osu!catch or osu!mania).")
                .keyword("--mode")
                .aliases(new String[]{"//mode"});
    }

    public OsuModeResolver() {
        super("mode", new Pattern[]{Pattern.compile("(?i)(?:--|//)mode(?:\\s+(.+))?")});
    }

    @Override
    public List<GameMode> findSorted(@NonNull CommandEvent event, String query) {
        return Finder.find(GameMode.values(), GameMode::getName, query);
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
