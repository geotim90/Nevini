package de.nevini.api.osu;

import de.nevini.api.ApiResponse;
import de.nevini.api.osu.model.*;
import de.nevini.api.osu.requests.OsuBeatmapsRequest;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class OsuBeatmapsTest extends OsuApiProvider {

    @Test
    public void testBeatmapId() {
        ApiResponse<List<OsuBeatmap>> response = getOsuApi().getBeatmaps(OsuBeatmapsRequest.builder()
                .beatmapId(252002)
                .build());
        Assert.assertTrue(response.isOk());

        // make sure the requested beatmap is there
        List<OsuBeatmap> result = response.getResult();
        Assert.assertEquals(1, result.size());

        // make sure all data was parsed correctly
        OsuBeatmap beatmap = result.get(0);
        Assert.assertEquals(Integer.valueOf(93398), beatmap.getBeatmapsetId());
        Assert.assertEquals(Integer.valueOf(252002), beatmap.getBeatmapId());
        Assert.assertEquals(OsuBeatmapApproved.RANKED, beatmap.getApproved());
        Assert.assertEquals(Integer.valueOf(146), beatmap.getTotalLength());
        Assert.assertEquals(Integer.valueOf(114), beatmap.getHitLength());
        Assert.assertEquals("Overkill", beatmap.getVersion());
        Assert.assertEquals("c8f08438204abfcdd1a748ebfae67421", beatmap.getFileMd5());
        Assert.assertEquals(Float.valueOf(4), beatmap.getDifficultySize());
        Assert.assertEquals(Float.valueOf(8), beatmap.getDifficultyOverall());
        Assert.assertEquals(Float.valueOf(9), beatmap.getDifficultyApproach());
        Assert.assertEquals(Float.valueOf(7), beatmap.getDifficultyDrain());
        Assert.assertEquals(OsuMode.STANDARD, beatmap.getMode());
        Assert.assertEquals(Integer.valueOf(388), beatmap.getCountNormal());
        Assert.assertEquals(Integer.valueOf(222), beatmap.getCountSlider());
        Assert.assertEquals(Integer.valueOf(3), beatmap.getCountSpinner());
        Assert.assertEquals(Date.from(ZonedDateTime.of(2013, 5, 15, 11, 32, 26, 0, ZoneOffset.UTC).toInstant()), beatmap.getSubmitDate());
        Assert.assertEquals(Date.from(ZonedDateTime.of(2013, 7, 6, 8, 54, 46, 0, ZoneOffset.UTC).toInstant()), beatmap.getApprovedDate());
        Assert.assertEquals(Date.from(ZonedDateTime.of(2013, 7, 6, 8, 51, 22, 0, ZoneOffset.UTC).toInstant()), beatmap.getLastUpdate());
        Assert.assertEquals("Luxion", beatmap.getArtist());
        Assert.assertEquals("High-Priestess", beatmap.getTitle());
        Assert.assertEquals("RikiH_", beatmap.getCreatorName());
        Assert.assertEquals(Integer.valueOf(686209), beatmap.getCreatorId());
        Assert.assertEquals(Float.valueOf(196), beatmap.getBpm());
        Assert.assertEquals("BMS", beatmap.getSource());
        Assert.assertArrayEquals(new String[]{"kloyd", "flower", "roxas"}, beatmap.getTags());
        Assert.assertEquals(OsuBeatmapGenre.VIDEO_GAME, beatmap.getGenre());
        Assert.assertEquals(OsuBeatmapLanguage.INSTRUMENTAL, beatmap.getLanguage());
        Assert.assertEquals(Integer.valueOf(142), beatmap.getFavouriteCount());
        Assert.assertEquals(Float.valueOf(9.45016f), beatmap.getRating());
        Assert.assertEquals(Boolean.FALSE, beatmap.getDownloadUnavailable());
        Assert.assertEquals(Boolean.FALSE, beatmap.getAudioUnavailable());
        Assert.assertEquals(Integer.valueOf(95168), beatmap.getPlayCount());
        Assert.assertEquals(Integer.valueOf(10644), beatmap.getPassCount());
        Assert.assertEquals(Integer.valueOf(899), beatmap.getMaxCombo());
        Assert.assertEquals(Float.valueOf(2.7706098556518555f), beatmap.getDifficultyAim());
        Assert.assertEquals(Float.valueOf(2.9062750339508057f), beatmap.getDifficultySpeed());
        Assert.assertEquals(Float.valueOf(5.744717597961426f), beatmap.getDifficultyRating());
        // TODO some information here may not be static over time (e.g. play count)
    }

}