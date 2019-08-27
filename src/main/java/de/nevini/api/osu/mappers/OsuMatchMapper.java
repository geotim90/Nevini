package de.nevini.api.osu.mappers;

import de.nevini.api.osu.external.model.OsuApiMatch;
import de.nevini.api.osu.external.model.OsuApiMatchGame;
import de.nevini.api.osu.external.model.OsuApiMatchGameScore;
import de.nevini.api.osu.model.*;
import lombok.NonNull;

import java.util.stream.Collectors;

import static de.nevini.api.osu.mappers.OsuMapperUtils.*;

public class OsuMatchMapper {

    public static @NonNull OsuMatch map(@NonNull OsuApiMatch match) {
        return new OsuMatch(
                match.getMatch().getMatchId(),
                match.getMatch().getName(),
                convertDate(match.getMatch().getStartTime()),
                convertDate(match.getMatch().getEndTime()),
                match.getGames().stream().map(OsuMatchMapper::mapGame).collect(Collectors.toList())
        );
    }

    private static @NonNull OsuGame mapGame(@NonNull OsuApiMatchGame game) {
        return new OsuGame(
                game.getGameId(),
                convertDate(game.getStartTime()),
                convertDate(game.getEndTime()),
                game.getBeatmapId(),
                convertMode(game.getMode()),
                convertMatchType(game.getMatchType()),
                convertScoringType(game.getScoringType()),
                convertTeamType(game.getTeamType()),
                convertMods(game.getMods()),
                game.getScores().stream().map(OsuMatchMapper::mapScore).collect(Collectors.toList())
        );
    }

    private static @NonNull OsuGameScore mapScore(@NonNull OsuApiMatchGameScore score) {
        return new OsuGameScore(
                score.getSlot(),
                convertTeam(score.getTeam()),
                score.getUserId(),
                score.getScore(),
                score.getMaxCombo(),
                score.getRank(),
                score.getCount50(),
                score.getCount100(),
                score.getCount300(),
                score.getCountMiss(),
                score.getCountKatu(),
                score.getCountGeki(),
                score.getPerfect(),
                score.getPass(),
                convertMods(score.getMods())
        );
    }

    private static OsuMatchType convertMatchType(Integer value) {
        if (value != null) {
            for (OsuMatchType matchType : OsuMatchType.values()) {
                if (matchType.getId() == value) {
                    return matchType;
                }
            }
        }
        return null;
    }

    private static OsuScoringType convertScoringType(Integer value) {
        if (value != null) {
            for (OsuScoringType scoringType : OsuScoringType.values()) {
                if (scoringType.getId() == value) {
                    return scoringType;
                }
            }
        }
        return null;
    }

    private static OsuTeam convertTeam(Integer value) {
        if (value != null) {
            for (OsuTeam team : OsuTeam.values()) {
                if (team.getId() == value) {
                    return team;
                }
            }
        }
        return null;
    }

    private static OsuTeamType convertTeamType(Integer value) {
        if (value != null) {
            for (OsuTeamType teamType : OsuTeamType.values()) {
                if (teamType.getId() == value) {
                    return teamType;
                }
            }
        }
        return null;
    }

}
