
package de.nevini.api.osu.model;

import lombok.Value;

@Value
public class OsuBeatmap {

    private final OsuStatus approved;
    private final Long submitDate;
    private final Long approvedDate;
    private final Long lastUpdate;
    private final String artist;
    private final Integer beatmapId;
    private final Integer beatmapsetId;
    private final Double bpm;
    private final String creatorName;
    private final Integer creatorId;
    private final Double difficultyRating;
    private final Double difficultyAim;
    private final Double difficultySpeed;
    private final Double difficultySize;
    private final Double difficultyOverall;
    private final Double difficultyApproach;
    private final Double difficultyDrain;
    private final Integer hitLength;
    private final String source;
    private final OsuGenre genre;
    private final OsuLanguage language;
    private final String title;
    private final Integer totalLength;
    private final String version;
    private final String fileMd5;
    private final OsuMode mode;
    private final String tags;
    private final Integer favouriteCount;
    private final Double rating;
    private final Integer playCount;
    private final Integer passCount;
    private final Integer countNormal;
    private final Integer countSlider;
    private final Integer countSpinner;
    private final Integer maxCombo;
    private final Double maxPp;
    private final Boolean downloadUnavailable;
    private final Boolean audioUnavailable;

}
