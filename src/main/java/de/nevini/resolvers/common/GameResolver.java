package de.nevini.resolvers.common;

import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.db.game.GameData;
import de.nevini.resolvers.AbstractResolver;
import lombok.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GameResolver extends AbstractResolver<GameData> {

    public static CommandOptionDescriptor.CommandOptionDescriptorBuilder describe() {
        return CommandOptionDescriptor.builder()
                .syntax("--game <game>")
                .description("Refers to a specific game. You can provide (part of) the name of a game or its application ID.")
                .keyword("--game")
                .aliases(new String[]{"//game"});
    }

    protected GameResolver() {
        super("game", new Pattern[]{Pattern.compile("(?i)(?:--|//)game(?:\\s+(.+))?")});
    }

    @Override
    public List<GameData> findSorted(@NonNull CommandEvent event, String query) {
        return event.getGameService().findGames(query).stream()
                .sorted(Comparator.comparing(GameData::getName)).collect(Collectors.toList());
    }

    @Override
    protected String getFieldNameForPicker(GameData item) {
        return item.getName();
    }

    @Override
    protected String getFieldValueForPicker(GameData item) {
        return Long.toUnsignedString(item.getId());
    }

}
