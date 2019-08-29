package de.nevini.api.osu.mappers;

import de.nevini.api.osu.external.model.OsuApiUser;
import de.nevini.api.osu.external.requests.OsuApiGetUserRequest;
import de.nevini.api.osu.jpa.user.OsuUserData;
import de.nevini.api.osu.jpa.user.OsuUserId;
import de.nevini.api.osu.model.OsuMode;
import de.nevini.api.osu.model.OsuUser;
import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Objects;

import static de.nevini.api.osu.mappers.OsuMapperUtils.convertDate;
import static de.nevini.api.osu.mappers.OsuMapperUtils.convertMode;

public class OsuUserMapper {

    public static @NonNull OsuUserData map(@NonNull OsuApiUser user, @NonNull OsuApiGetUserRequest request) {
        return map(user, new OsuUserId(
                user.getUserId(),
                ObjectUtils.defaultIfNull(request.getMode(), OsuMode.STANDARD.getId())
        ));
    }

    private static @NonNull OsuUserData map(@NonNull OsuApiUser user, @NonNull OsuUserId id) {
        assert Objects.equals(user.getUserId(), id.getUserId());
        return new OsuUserData(
                user.getUserId(),
                id.getMode(),
                user.getUserName(),
                convertDate(user.getJoinDate()),
                user.getCount300(),
                user.getCount100(),
                user.getCount50(),
                user.getPlayCount(),
                user.getRankedScore(),
                user.getTotalScore(),
                user.getPpRank(),
                user.getLevel(),
                user.getPpRaw(),
                user.getAccuracy(),
                user.getCountRankSs(),
                user.getCountRankSsh(),
                user.getCountRankS(),
                user.getCountRankSh(),
                user.getCountRankA(),
                user.getCountry(),
                user.getTotalSecondsPlayed(),
                user.getPpCountryRank()
        );
    }

    public static @NonNull OsuUser map(@NonNull OsuUserData data) {
        return new OsuUser(
                data.getUserId(),
                convertMode(data.getMode()),
                data.getUserName(),
                data.getJoinDate(),
                data.getCount300(),
                data.getCount100(),
                data.getCount50(),
                data.getPlayCount(),
                data.getRankedScore(),
                data.getTotalScore(),
                data.getPpRank(),
                data.getLevel(),
                data.getPpRaw(),
                data.getAccuracy(),
                data.getCountRankSs(),
                data.getCountRankSsh(),
                data.getCountRankS(),
                data.getCountRankSh(),
                data.getCountRankA(),
                data.getCountry(),
                data.getTotalSecondsPlayed(),
                data.getPpCountryRank()
        );
    }

}
