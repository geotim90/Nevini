package de.nevini.api.osu.external.requests;

import de.nevini.api.ApiResponse;
import de.nevini.api.osu.external.model.OsuApiUserBest;
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

public class OsuUserBestTest extends OsuApiProvider {

    @Test
    public void testParser() throws IOException {
        OsuApiGetUserBestRequest request = OsuApiGetUserBestRequest.builder()
                .user("2")
                .build();
        List<OsuApiUserBest> result;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("get_user_best_u_2.json")
        )) {
            result = request.parseStream(reader);
        }

        // make sure all 10 bests are there
        Assert.assertEquals(10, result.size());

        // make sure all data was parsed correctly
        OsuApiUserBest best = result.get(0);
        Assert.assertEquals(Integer.valueOf(22423), best.getBeatmapId());
        Assert.assertEquals(Long.valueOf(1720541511L), best.getScoreId());
        Assert.assertEquals(Integer.valueOf(2669868), best.getScore());
        Assert.assertEquals(Integer.valueOf(450), best.getMaxCombo());
        Assert.assertEquals(Integer.valueOf(0), best.getCount50());
        Assert.assertEquals(Integer.valueOf(0), best.getCount100());
        Assert.assertEquals(Integer.valueOf(254), best.getCount300());
        Assert.assertEquals(Integer.valueOf(0), best.getCountMiss());
        Assert.assertEquals(Integer.valueOf(0), best.getCountKatu());
        Assert.assertEquals(Integer.valueOf(38), best.getCountGeki());
        Assert.assertEquals(Boolean.TRUE, best.getPerfect());
        Assert.assertEquals(Integer.valueOf(OsuMod.sum(OsuMod.SUDDEN_DEATH, OsuMod.PERFECT)), best.getMods());
        Assert.assertEquals(Integer.valueOf(2), best.getUserId());
        Assert.assertEquals(Date.from(ZonedDateTime.of(2014, 3, 25, 3, 58, 4, 0, ZoneOffset.UTC).toInstant()), best.getDate());
        Assert.assertEquals(OsuRank.SS.getId(), best.getRank());
        Assert.assertEquals(Double.valueOf(70.2194), best.getPp());
    }

    @Test
    public void testUserIdRequest() {
        ApiResponse<List<OsuApiUserBest>> response = getOsuApi().getUserBest(OsuApiGetUserBestRequest.builder()
                .user("2")
                .build());
        Assert.assertTrue(response.toString(), response.isOk());

        // make sure the default 10 beatmaps are there
        List<OsuApiUserBest> result = response.getResult();
        Assert.assertEquals(10, result.size());

        // make sure all data was parsed correctly
        OsuApiUserBest best = result.get(0);
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
