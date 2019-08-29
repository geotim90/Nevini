package de.nevini.api.osu.mappers;

import de.nevini.api.osu.external.model.OsuApiScore;
import de.nevini.api.osu.external.requests.OsuApiGetScoresRequest;
import de.nevini.api.osu.jpa.score.OsuScoreData;
import de.nevini.api.osu.jpa.score.OsuScoreId;
import de.nevini.api.osu.model.OsuMode;
import de.nevini.api.osu.model.OsuScore;
import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;

import static de.nevini.api.osu.mappers.OsuMapperUtils.convertDate;

public class OsuScoreMapper {

    public static @NonNull OsuScoreData map(@NonNull OsuApiScore score, @NonNull OsuApiGetScoresRequest request) {
        return map(score, new OsuScoreId(
                request.getBeatmapId(),
                ObjectUtils.defaultIfNull(request.getMode(), OsuMode.STANDARD.getId()),
                score.getMods()
        ));
    }

    private static @NonNull OsuScoreData map(@NonNull OsuApiScore score, @NonNull OsuScoreId id) {
        return new OsuScoreData(
                score.getScoreId(),
                id.getBeatmapId(),
                id.getMode(),
                id.getMods(),
                score.getUserId(),
                score.getUserName(),
                score.getScore(),
                score.getCount300(),
                score.getCount100(),
                score.getCount50(),
                score.getCountMiss(),
                score.getMaxCombo(),
                score.getCountKatu(),
                score.getCountGeki(),
                score.getPerfect(),
                convertDate(score.getDate()),
                score.getRank(),
                score.getPp(),
                score.getReplayAvailable()
        );
    }

    public static @NonNull OsuScore map(@NonNull OsuScoreData score) {
        return new OsuScore(
                score.getScoreId(),
                score.getBeatmapId(),
                OsuMapperUtils.convertMode(score.getMode()),
                OsuMapperUtils.convertMods(score.getMods()),
                score.getUserId(),
                score.getUserName(),
                score.getScore(),
                score.getCount300(),
                score.getCount100(),
                score.getCount50(),
                score.getCountMiss(),
                score.getMaxCombo(),
                score.getCountKatu(),
                score.getCountGeki(),
                score.getPerfect(),
                score.getDate(),
                OsuMapperUtils.convertRank(score.getRank()),
                score.getPp(),
                score.getReplayAvailable()
        );
    }

}
