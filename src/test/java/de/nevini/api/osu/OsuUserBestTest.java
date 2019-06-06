package de.nevini.api.osu;

import de.nevini.api.ApiResponse;
import de.nevini.api.osu.model.OsuUserBest;
import de.nevini.api.osu.requests.OsuUserBestRequest;
import org.junit.Assert;
import org.junit.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class OsuUserBestTest extends OsuApiProvider {

    @Test
    public void testUserIdRequest() {
        ApiResponse<List<OsuUserBest>> response = getOsuApi().getUserBest(OsuUserBestRequest.builder()
                .user("2")
                .build());
        Assert.assertTrue(response.toString(), response.isOk());

        // make sure the default 10 beatmaps are there
        List<OsuUserBest> result = response.getResult();
        Assert.assertEquals(10, result.size());

        // make sure all data was parsed correctly
        OsuUserBest best = result.get(0);
        Assert.assertTrue(best.getBeatmapId() >= 0);
        Assert.assertTrue(best.getScoreId() >= 1720541511L); // new scores will always have higher ids
        Assert.assertTrue(best.getScore() >= 0);
        Assert.assertTrue(best.getMaxCombo() >= 0);
        Assert.assertTrue(best.getCount50() >= 0);
        Assert.assertTrue(best.getCount100() >= 0);
        Assert.assertTrue(best.getCount300() >= 0);
        Assert.assertTrue(best.getCountMiss() >= 0);
        Assert.assertTrue(best.getCountKatu() >= 0);
        Assert.assertTrue(best.getCountGeki() >= 0);
        Assert.assertNotNull(best.getPerfect());
        Assert.assertNotNull(best.getMods()); // should be empty if no mods were selected
        Assert.assertEquals(Integer.valueOf(2), best.getUserId());
        Assert.assertTrue(best.getDate().getTime() >= ZonedDateTime.of(2014, 3, 25, 3, 58, 4, 0, ZoneOffset.UTC).toInstant().toEpochMilli()); // new scores will always be newer
        Assert.assertNotNull(best.getRank());
        Assert.assertTrue(best.getPp() > 0f);
    }

}
