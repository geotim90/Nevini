package de.nevini.api.osu.external.requests;

import de.nevini.api.ApiResponse;
import de.nevini.api.osu.external.model.OsuApiUserRecent;
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

public class OsuUserRecentTest extends OsuApiProvider {

    @Test
    public void testParser() throws IOException {
        OsuApiGetUserRecentRequest request = OsuApiGetUserRecentRequest.builder()
                .user("2")
                .build();
        List<OsuApiUserRecent> result;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("get_user_recent_u_1883865.json")
        )) {
            result = request.parseStream(reader);
        }

        // make sure all 10 bests are there
        Assert.assertEquals(10, result.size());

        // make sure all data was parsed correctly
        OsuApiUserRecent best = result.get(0);
        Assert.assertEquals(Integer.valueOf(1706996), best.getBeatmapId());
        Assert.assertEquals(Integer.valueOf(7800173), best.getScore());
        Assert.assertEquals(Integer.valueOf(597), best.getMaxCombo());
        Assert.assertEquals(Integer.valueOf(0), best.getCount50());
        Assert.assertEquals(Integer.valueOf(4), best.getCount100());
        Assert.assertEquals(Integer.valueOf(403), best.getCount300());
        Assert.assertEquals(Integer.valueOf(13), best.getCountMiss());
        Assert.assertEquals(Integer.valueOf(4), best.getCountKatu());
        Assert.assertEquals(Integer.valueOf(108), best.getCountGeki());
        Assert.assertEquals(Boolean.FALSE, best.getPerfect());
        Assert.assertEquals(Integer.valueOf(OsuMod.HIDDEN.getId()), best.getMods());
        Assert.assertEquals(Integer.valueOf(1883865), best.getUserId());
        Assert.assertEquals(Date.from(ZonedDateTime.of(2019, 6, 5, 20, 26, 17, 0, ZoneOffset.UTC).toInstant()), best.getDate());
        Assert.assertEquals(OsuRank.F.getId(), best.getRank());
    }

    @Test
    public void testUserIdRequest() {
        ApiResponse<List<OsuApiUserRecent>> response = getOsuApi().getUserRecent(OsuApiGetUserRecentRequest.builder()
                .user("2")
                .build());
        Assert.assertTrue(response.toString(), response.isOk());

        // unfortunately only goes back 24 hours and is often empty but should never be null
        Assert.assertNotNull(response.getResult());
    }

}
