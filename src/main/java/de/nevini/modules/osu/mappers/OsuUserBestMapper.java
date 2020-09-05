package de.nevini.modules.osu.mappers;

import de.nevini.modules.osu.api.model.OsuApiUserBest;
import de.nevini.modules.osu.api.requests.OsuApiGetUserBestRequest;
import de.nevini.modules.osu.model.OsuUserBest;
import lombok.NonNull;

import static de.nevini.modules.osu.mappers.OsuMapperUtils.*;

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
