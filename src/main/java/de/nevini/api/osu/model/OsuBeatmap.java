
package de.nevini.api.osu.model;

import lombok.Value;

@Value
public class OsuBeatmap {

    OsuStatus approved;
    Long submitDate;
    Long approvedDate;
    Long lastUpdate;
    String artist;
    Integer beatmapId;
    Integer beatmapsetId;
    Double bpm;
    String creatorName;
    Integer creatorId;
    Double difficultyRating;
    Double difficultyAim;
    Double difficultySpeed;
    Double difficultySize;
    Double difficultyOverall;
    Double difficultyApproach;
    Double difficultyDrain;
    Integer hitLength;
    String source;
    OsuGenre genre;
    OsuLanguage language;
    String title;
    Integer totalLength;
    String version;
    String fileMd5;
    OsuMode mode;
    OsuMode convertedMode;
    OsuMod[] mods;
    String tags;
    Integer favouriteCount;
    Double rating;
    Integer playCount;
    Integer passCount;
    Integer countNormal;
    Integer countSlider;
    Integer countSpinner;
    Integer maxCombo;
    Double maxPp;
    Boolean downloadUnavailable;
    Boolean audioUnavailable;

}
