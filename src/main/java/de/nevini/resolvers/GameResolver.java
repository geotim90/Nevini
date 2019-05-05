package de.nevini.resolvers;

import de.nevini.command.CommandEvent;
import de.nevini.db.game.GameData;
import lombok.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GameResolver extends AbstractResolver<GameData> {

    public GameResolver() {
        super("game", new Pattern[]{Pattern.compile("(?i)(?:--|//)game(?:\\s+(.+))")});
    }

    @Override
    protected List<GameData> findSorted(@NonNull CommandEvent event, String query) {
        return event.getGameService().findGames(query).stream()
                .sorted(Comparator.comparing(GameData::getName)).collect(Collectors.toList());
    }

}
