package de.nevini.api.osu;

import de.nevini.api.ApiResponse;
import de.nevini.api.osu.model.OsuScore;
import de.nevini.api.osu.requests.OsuScoresRequest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Ignore("Endpoint returns empty array on POST")
public class OsuScoreTest extends OsuApiProvider {

    @Test
    public void testBeatmapIdRequest() {
        ApiResponse<List<OsuScore>> response = getOsuApi().getScores(OsuScoresRequest.builder()
                .beatmapId(252002)
                .build());
        Assert.assertTrue(response.toString(), response.isOk());

        // make sure the default 50 beatmaps are there
        List<OsuScore> result = response.getResult();
        Assert.assertEquals(50, result.size());

        // make sure all data was parsed correctly
        OsuScore score = result.get(0);
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
