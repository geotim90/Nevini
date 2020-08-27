package de.nevini.modules.osu.mappers;

import de.nevini.modules.osu.api.model.OsuApiReplay;
import de.nevini.modules.osu.api.requests.OsuApiGetReplayRequest;
import de.nevini.modules.osu.model.OsuReplay;
import lombok.NonNull;

import static de.nevini.modules.osu.mappers.OsuMapperUtils.convertMode;
import static de.nevini.modules.osu.mappers.OsuMapperUtils.convertMods;

public class OsuReplayMapper {

    public static @NonNull OsuReplay map(@NonNull OsuApiReplay replay, @NonNull OsuApiGetReplayRequest request) {
        return new OsuReplay(
                convertMode(request.getMode()),
                request.getBeatmapId(),
                request.getUser(),
                request.getUserType(),
                convertMods(request.getMods()),
                replay.getContent(),
                replay.getEncoding()
        );
    }

}
