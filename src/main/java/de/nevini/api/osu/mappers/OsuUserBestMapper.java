package de.nevini.api.osu.mappers;

import de.nevini.api.osu.external.model.OsuApiUserBest;
import de.nevini.api.osu.external.requests.OsuApiGetUserBestRequest;
import de.nevini.api.osu.model.OsuUserBest;
import lombok.NonNull;

import static de.nevini.api.osu.mappers.OsuMapperUtils.*;

public class OsuUserBestMapper {

    public static @NonNull OsuUserBest map(@NonNull OsuApiUserBest best, @NonNull OsuApiGetUserBestRequest request) {
        return new OsuUserBest(
                request.getUser(),
                request.getUserType(),
                convertMode(request.getMode()),
                best.getBeatmapId(),
                best.getScoreId(),
                best.getScore(),
                best.getMaxCombo(),
                best.getCount50(),
                best.getCount100(),
                best.getCount300(),
                best.getCountMiss(),
                best.getCountKatu(),
                best.getCountGeki(),
                best.getPerfect(),
                convertMods(best.getMods()),
                best.getUserId(),
                convertDate(best.getDate()),
                convertRank(best.getRank()),
                best.getPp()
        );
    }

}
