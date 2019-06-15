package de.nevini.bot.data.osu.beatmap;

import de.nevini.api.osu.model.*;
import de.nevini.bot.db.osu.beatmap.OsuBeatmapData;
import org.apache.commons.lang3.StringUtils;

import static de.nevini.bot.data.osu.OsuMapperUtils.convertDate;
import static de.nevini.bot.data.osu.OsuMapperUtils.convertFloat;

public class OsuBeatmapDataMapper {

    public static OsuBeatmap convert(OsuBeatmapData data) {
        if (data == null) return null;
        return OsuBeatmap.builder()
                .approved(convertApproved(data.getApproved()))
                .submitDate(convertDate(data.getSubmitDate()))
                .approvedDate(convertDate(data.getApprovedDate()))
                .lastUpdate(convertDate(data.getLastUpdate()))
                .artist(data.getArtist())
                .beatmapId(data.getBeatmapId())
                .beatmapsetId(data.getBeatmapsetId())
                .bpm(convertFloat(data.getBpm()))
                .creatorName(data.getCreatorName())
                .creatorId(data.getCreatorId())
                .difficultyRating(convertFloat(data.getDifficultyRating()))
                .difficultyAim(convertFloat(data.getDifficultyAim()))
                .difficultySpeed(convertFloat(data.getDifficultySpeed()))
                .difficultySize(convertFloat(data.getDifficultySize()))
                .difficultyOverall(convertFloat(data.getDifficultyOverall()))
                .difficultyApproach(convertFloat(data.getDifficultyApproach()))
                .difficultyDrain(convertFloat(data.getDifficultyDrain()))
                .hitLength(data.getHitLength())
                .source(data.getSource())
                .genre(convertGenre(data.getGenre()))
                .language(convertLanguage(data.getLanguage()))
                .title(data.getTitle())
                .totalLength(data.getTotalLength())
                .version(data.getVersion())
                .fileMd5(data.getFileMd5())
                .mode(convertMode(data.getMode()))
                .tags(convertTags(data.getTags()))
                .favouriteCount(data.getFavouriteCount())
                .rating(convertFloat(data.getRating()))
                .playCount(data.getPlayCount())
                .passCount(data.getPassCount())
                .countNormal(data.getCountNormal())
                .countSlider(data.getCountSlider())
                .countSpinner(data.getCountSpinner())
                .maxCombo(data.getMaxCombo())
                .downloadUnavailable(data.getDownloadUnavailable())
                .audioUnavailable(data.getAudioUnavailable())
                .build();
    }

    private static OsuBeatmapApproved convertApproved(Integer value) {
        if (value == null) return null;
        for (OsuBeatmapApproved e : OsuBeatmapApproved.values()) {
            if (e.getId() == value) {
                return e;
            }
        }
        throw new IllegalArgumentException(value + " is not a valid approved id");
    }

    private static OsuBeatmapGenre convertGenre(Integer value) {
        if (value == null) return null;
        for (OsuBeatmapGenre e : OsuBeatmapGenre.values()) {
            if (e.getId() == value) {
                return e;
            }
        }
        throw new IllegalArgumentException(value + " is not a valid genre id");
    }

    private static OsuBeatmapLanguage convertLanguage(Integer value) {
        if (value == null) return null;
        for (OsuBeatmapLanguage e : OsuBeatmapLanguage.values()) {
            if (e.getId() == value) {
                return e;
            }
        }
        throw new IllegalArgumentException(value + " is not a valid genre id");
    }

    private static OsuMode convertMode(Integer value) {
        if (value == null) return null;
        for (OsuMode e : OsuMode.values()) {
            if (e.getId() == value) {
                return e;
            }
        }
        throw new IllegalArgumentException(value + " is not a valid genre id");
    }

    private static String[] convertTags(String value) {
        return StringUtils.split(value, ' ');
    }

    public static OsuBeatmapData convert(OsuBeatmap beatmap) {
        if (beatmap == null) return null;
        OsuBeatmapData data = new OsuBeatmapData();
        data.setApproved(convertApproved(beatmap.getApproved()));
        data.setSubmitDate(convertDate(beatmap.getSubmitDate()));
        data.setApprovedDate(convertDate(beatmap.getApprovedDate()));
        data.setLastUpdate(convertDate(beatmap.getLastUpdate()));
        data.setArtist(beatmap.getArtist());
        data.setBeatmapId(beatmap.getBeatmapId());
        data.setBeatmapsetId(beatmap.getBeatmapsetId());
        data.setBpm(convertFloat(beatmap.getBpm()));
        data.setCreatorName(beatmap.getCreatorName());
        data.setCreatorId(beatmap.getCreatorId());
        data.setDifficultyRating(convertFloat(beatmap.getDifficultyRating()));
        data.setDifficultyAim(convertFloat(beatmap.getDifficultyAim()));
        data.setDifficultySpeed(convertFloat(beatmap.getDifficultySpeed()));
        data.setDifficultySize(convertFloat(beatmap.getDifficultySize()));
        data.setDifficultyOverall(convertFloat(beatmap.getDifficultyOverall()));
        data.setDifficultyApproach(convertFloat(beatmap.getDifficultyApproach()));
        data.setDifficultyDrain(convertFloat(beatmap.getDifficultyDrain()));
        data.setHitLength(beatmap.getHitLength());
        data.setSource(beatmap.getSource());
        data.setGenre(convertGenre(beatmap.getGenre()));
        data.setLanguage(convertLanguage(beatmap.getLanguage()));
        data.setTitle(beatmap.getTitle());
        data.setTotalLength(beatmap.getTotalLength());
        data.setVersion(beatmap.getVersion());
        data.setFileMd5(beatmap.getFileMd5());
        data.setMode(convertMode(beatmap.getMode()));
        data.setTags(convertTags(beatmap.getTags()));
        data.setFavouriteCount(beatmap.getFavouriteCount());
        data.setRating(convertFloat(beatmap.getRating()));
        data.setPlayCount(beatmap.getPlayCount());
        data.setPassCount(beatmap.getPassCount());
        data.setCountNormal(beatmap.getCountNormal());
        data.setCountSlider(beatmap.getCountSlider());
        data.setCountSpinner(beatmap.getCountSpinner());
        data.setMaxCombo(beatmap.getMaxCombo());
        data.setDownloadUnavailable(beatmap.getDownloadUnavailable());
        data.setAudioUnavailable(beatmap.getAudioUnavailable());
        return data;
    }

    private static Integer convertApproved(OsuBeatmapApproved value) {
        return value == null ? null : value.getId();
    }

    private static Integer convertGenre(OsuBeatmapGenre value) {
        return value == null ? null : value.getId();
    }

    private static Integer convertLanguage(OsuBeatmapLanguage value) {
        return value == null ? null : value.getId();
    }

    private static Integer convertMode(OsuMode value) {
        return value == null ? null : value.getId();
    }

    private static String convertTags(String[] value) {
        return StringUtils.join(value, ' ');
    }

}
