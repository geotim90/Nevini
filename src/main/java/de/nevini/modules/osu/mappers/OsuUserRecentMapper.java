package de.nevini.modules.osu.mappers;

import de.nevini.modules.osu.api.model.OsuApiUserRecent;
import de.nevini.modules.osu.api.requests.OsuApiGetUserRecentRequest;
import de.nevini.modules.osu.model.OsuUserRecent;
import lombok.NonNull;

import static de.nevini.modules.osu.mappers.OsuMapperUtils.*;

public class OsuUserRecentMapper {

    public static @NonNull OsuUserRecent map(
            @NonNull OsuApiUserRecent recent, @NonNull OsuApiGetUserRecentRequest request
    ) {
        return new OsuUserRecent(
                request.getUser(),
                request.getUserType(),
                convertMode(request.getMode()),
                recent.getBeatmapId(),
                recent.getScore(),
                recent.getMaxCombo(),
                recent.getCount50(),
                recent.getCount100(),
                recent.getCount300(),
                recent.getCountMiss(),
                recent.getCountKatu(),
                recent.getCountGeki(),
                recent.getPerfect(),
                convertMods(recent.getMods()),
                recent.getUserId(),
                convertDate(recent.getDate()),
                convertRank(recent.getRank())
        );
    }

}
