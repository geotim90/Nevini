package de.nevini.api.osu.external.requests;

import de.nevini.api.ApiResponse;
import de.nevini.api.osu.external.model.OsuApiScore;
import de.nevini.api.osu.model.OsuMod;
import de.nevini.api.osu.model.OsuRank;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public class OsuScoreTest extends OsuApiProvider {

    @Test
    public void testParser() throws IOException {
        OsuApiGetScoresRequest request = OsuApiGetScoresRequest.builder()
                .beatmapId(252002)
                .build();
        List<OsuApiScore> result;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("get_scores_b_252002.json")
        )) {
            result = request.parseStream(reader);
        }

        // make sure all 50 beatmaps are there
        Assert.assertEquals(50, result.size());

        // make sure all data was parsed correctly
        OsuApiScore score = result.get(0);
        Assert.assertEquals(Long.valueOf(2375887323L), score.getScoreId());
        Assert.assertEquals(Integer.valueOf(19412505), score.getScore());
        Assert.assertEquals("Wilchq", score.getUserName());
        Assert.assertEquals(Integer.valueOf(899), score.getMaxCombo());
        Assert.assertEquals(Integer.valueOf(0), score.getCount50());
        Assert.assertEquals(Integer.valueOf(4), score.getCount100());
        Assert.assertEquals(Integer.valueOf(609), score.getCount300());
        Assert.assertEquals(Integer.valueOf(0), score.getCountMiss());
        Assert.assertEquals(Integer.valueOf(4), score.getCountKatu());
        Assert.assertEquals(Integer.valueOf(97), score.getCountGeki());
        Assert.assertEquals(Boolean.TRUE, score.getPerfect());
        Assert.assertEquals(Integer.valueOf(OsuMod.sum(OsuMod.HIDDEN, OsuMod.HARD_ROCK)), score.getMods());
        Assert.assertEquals(Integer.valueOf(2021758), score.getUserId());
        Assert.assertEquals(Date.from(ZonedDateTime.of(2017, 9, 24, 13, 28, 31, 0, ZoneOffset.UTC).toInstant()), score.getDate());
        Assert.assertEquals(OsuRank.SH.getId(), score.getRank());
        Assert.assertEquals(Double.valueOf(377.399), score.getPp());
        Assert.assertEquals(Boolean.TRUE, score.getReplayAvailable());
    }

    @Test
    public void testBeatmapIdRequest() {
        ApiResponse<List<OsuApiScore>> response = getOsuApi().getScores(OsuApiGetScoresRequest.builder()
                .beatmapId(252002)
                .build());
        Assert.assertTrue(response.toString(), response.isOk());

        // make sure the default 50 beatmaps are there
        List<OsuApiScore> result = response.getResult();
        Assert.assertEquals(50, result.size());

        // make sure all data was parsed correctly
        OsuApiScore score = result.get(0);
        Assert.assertTrue(score.getScoreId() >= 2375887323L); // new scores will always have higher ids
        Assert.assertTrue(score.getScore() >= 19412505); // new scores will always have higher scores
        Assert.assertNotNull(score.getUserName());
        Assert.assertTrue(score.getMaxCombo() >= 0);
        Assert.assertTrue(score.getCount50() >= 0);
        Assert.assertTrue(score.getCount100() >= 0);
        Assert.assertTrue(score.getCount300() >= 0);
        Assert.assertTrue(score.getCountMiss() >= 0);
        Assert.assertTrue(score.getCountKatu() >= 0);
        Assert.assertTrue(score.getCountGeki() >= 0);
        Assert.assertNotNull(score.getPerfect());
        Assert.assertNotNull(score.getMods()); // should be empty if no mods were selected
        Assert.assertTrue(score.getUserId() >= 2); // peppy has the lowest user id
        Assert.assertTrue(score.getDate().getTime() >= ZonedDateTime.of(2017, 9, 24, 13, 28, 31, 0, ZoneOffset.UTC).toInstant().toEpochMilli()); // new scores will always be newer
        Assert.assertNotNull(score.getRank());
        Assert.assertTrue(score.getPp() > 0f);
        Assert.assertNotNull(score.getReplayAvailable());
    }

}
