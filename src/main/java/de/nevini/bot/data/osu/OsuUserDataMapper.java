package de.nevini.bot.data.osu;

import de.nevini.api.osu.model.OsuMode;
import de.nevini.api.osu.model.OsuUser;
import de.nevini.bot.db.osu.user.OsuUserData;

import static de.nevini.bot.data.osu.OsuMapperUtils.convertDate;
import static de.nevini.bot.data.osu.OsuMapperUtils.convertFloat;

public class OsuUserDataMapper {

    public static OsuUser convert(OsuUserData data) {
        if (data == null) return null;
        return OsuUser.builder()
                .userId(data.getUserId())
                .userName(data.getUserName())
                .joinDate(convertDate(data.getJoinDate()))
                .count300(data.getCount300())
                .count100(data.getCount100())
                .count50(data.getCount50())
                .playCount(data.getPlayCount())
                .rankedScore(data.getRankedScore())
                .totalScore(data.getTotalScore())
                .ppRank(data.getPpRank())
                .level(convertFloat(data.getLevel()))
                .ppRaw(convertFloat(data.getPpRaw()))
                .accuracy(convertFloat(data.getAccuracy()))
                .countRankSs(data.getCountRankSs())
                .countRankSsh(data.getCountRankSsh())
                .countRankS(data.getCountRankS())
                .countRankSh(data.getCountRankSh())
                .countRankA(data.getCountRankA())
                .country(data.getCountry())
                .totalSecondsPlayed(data.getTotalSecondsPlayed())
                .ppCountryRank(data.getPpCountryRank())
                .build();
    }

    public static OsuUserData convert(OsuUser user, OsuMode mode) {
        if (user == null) return null;
        OsuUserData data = new OsuUserData();
        data.setUserId(user.getUserId());
        if (mode != null) data.setMode(mode.getId());
        data.setUserName(user.getUserName());
        data.setJoinDate(convertDate(user.getJoinDate()));
        data.setCount300(user.getCount300());
        data.setCount100(user.getCount100());
        data.setCount50(user.getCount50());
        data.setPlayCount(user.getPlayCount());
        data.setRankedScore(user.getRankedScore());
        data.setTotalScore(user.getTotalScore());
        data.setPpRank(user.getPpRank());
        data.setLevel(convertFloat(user.getLevel()));
        data.setPpRaw(convertFloat(user.getPpRaw()));
        data.setAccuracy(convertFloat(user.getAccuracy()));
        data.setCountRankSs(user.getCountRankSs());
        data.setCountRankSsh(user.getCountRankSsh());
        data.setCountRankS(user.getCountRankS());
        data.setCountRankSh(user.getCountRankSh());
        data.setCountRankA(user.getCountRankA());
        data.setCountry(user.getCountry());
        data.setTotalSecondsPlayed(user.getTotalSecondsPlayed());
        data.setPpCountryRank(user.getPpCountryRank());
        return data;
    }

}
