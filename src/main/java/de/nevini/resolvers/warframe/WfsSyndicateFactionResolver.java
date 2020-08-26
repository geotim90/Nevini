package de.nevini.resolvers.warframe;

import de.nevini.command.CommandEvent;
import de.nevini.resolvers.OptionResolver;
import de.nevini.util.Finder;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WfsSyndicateFactionResolver extends OptionResolver<String> {

    private static final List<String> FACTIONS = Arrays.asList(
            "Arbiters of Hexis",
            "Cephalon Suda",
            "New Loka",
            "Perrin Sequence",
            "Red Veil",
            "Steel Meridian"
    );

    WfsSyndicateFactionResolver() {
        super("faction", new Pattern[]{
                Pattern.compile("(?i)(?:--|//)faction(?:\\s+(.+))?")
        });
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax(argument ? "[--faction] <name>" : "--faction <name>")
                .description("Refers to " + (list ? "all factions" : "a faction") + " with a matching name."
                        + (argument ? "\nThe `--faction` flag is optional if this option is provided first." : ""))
                .keyword("--faction")
                .aliases(new String[]{"//faction"})
                .build();
    }

    @Override
    public List<String> findSorted(@NonNull CommandEvent event, String query) {
        return Finder.findLenient(FACTIONS, query).stream().sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    @Override
    protected @NonNull String getFieldNameForPicker(String item) {
        return item;
    }

    @Override
    protected @NonNull String getFieldValueForPicker(String item) {
        return "";
    }

}
