package de.nevini.resolvers.common;

import de.nevini.command.CommandEvent;
import de.nevini.jpa.game.GameData;
import de.nevini.resolvers.AbstractResolver;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GameResolver extends AbstractResolver<GameData> {

    GameResolver() {
        super("game", new Pattern[]{Pattern.compile("(?i)(?:--|//)game(?:\\s+(.+))?")});
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax(argument ? "[--game] <game>" : "--game <game>")
                .description("Refers to " + (list ? "all games" : "a specific game")
                        + " with a matching id or name."
                        + (argument ? "\nThe `--game` flag is optional if this option is provided first." : ""))
                .keyword("--game")
                .aliases(new String[]{"//game"})
                .build();
    }

    @Override
    public List<GameData> findSorted(@NonNull CommandEvent event, String query) {
        return event.getGameService().findGames(query).stream()
                .sorted(Comparator.comparing(GameData::getName)).collect(Collectors.toList());
    }

    @Override
    protected @NonNull String getFieldNameForPicker(GameData item) {
        return item.getName();
    }

    @Override
    protected @NonNull String getFieldValueForPicker(GameData item) {
        return Long.toUnsignedString(item.getId());
    }

}
