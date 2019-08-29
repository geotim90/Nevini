package de.nevini.api.osu.external.requests;

import de.nevini.api.ApiResponse;
import de.nevini.api.osu.external.model.OsuApiUser;
import de.nevini.api.osu.external.model.OsuApiUserEvent;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public class OsuUserTest extends OsuApiProvider {

    @Test
    public void testParser() throws IOException {
        OsuApiGetUserRequest request = OsuApiGetUserRequest.builder()
                .user("252002")
                .build();
        ApiResponse<List<OsuApiUser>> response = getOsuApi().getUser(request);
        Assert.assertTrue(response.toString(), response.isOk());

        List<OsuApiUser> result;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("get_user_u_1883865.json")
        )) {
            result = request.parseStream(reader);
        }

        // make sure the requested user is there
        Assert.assertEquals(1, result.size());

        // make sure all data was parsed correctly
        OsuApiUser user = result.get(0);
        Assert.assertEquals(Integer.valueOf(1883865), user.getUserId());
        Assert.assertEquals("Yaong", user.getUserName());
        Assert.assertEquals(Date.from(ZonedDateTime.of(2012, 8, 30, 12, 14, 56, 0, ZoneOffset.UTC).toInstant()), user.getJoinDate());
        Assert.assertEquals(Integer.valueOf(31556263), user.getCount300());
        Assert.assertEquals(Integer.valueOf(1285294), user.getCount100());
        Assert.assertEquals(Integer.valueOf(186314), user.getCount50());
        Assert.assertEquals(Integer.valueOf(112559), user.getPlayCount());
        Assert.assertEquals(Long.valueOf(105476892039L), user.getRankedScore());
        Assert.assertEquals(Long.valueOf(523275047004L), user.getTotalScore());
        Assert.assertEquals(Integer.valueOf(69), user.getPpRank());
        Assert.assertEquals(Double.valueOf(104.963), user.getLevel());
        Assert.assertEquals(Double.valueOf(12464.3), user.getPpRaw());
        Assert.assertEquals(Double.valueOf(99.44173431396484), user.getAccuracy());
        Assert.assertEquals(Integer.valueOf(724), user.getCountRankSs());
        Assert.assertEquals(Integer.valueOf(436), user.getCountRankSsh());
        Assert.assertEquals(Integer.valueOf(1690), user.getCountRankS());
        Assert.assertEquals(Integer.valueOf(1387), user.getCountRankSh());
        Assert.assertEquals(Integer.valueOf(454), user.getCountRankA());
        Assert.assertEquals("KR", user.getCountry());
        Assert.assertEquals(Integer.valueOf(6070490), user.getTotalSecondsPlayed());
        Assert.assertEquals(Integer.valueOf(7), user.getPpCountryRank());

        // make sure the requested user events are there
        Assert.assertEquals(17, user.getEvents().size());

        // make sure all data was parsed correctly
        OsuApiUserEvent event = user.getEvents().get(0);
        Assert.assertEquals("<b><a href='/u/1883865'>Yaong</a></b> has lost first place on <a href='/b/201700?m=0'>Leftymonster - START [Finish]</a> (osu!)", event.getDisplayHtml());
        Assert.assertEquals(Integer.valueOf(201700), event.getBeatmapId());
        Assert.assertEquals(Integer.valueOf(70102), event.getBeatmapsetId());
        Assert.assertEquals(Date.from(ZonedDateTime.of(2019, 6, 6, 2, 17, 33, 0, ZoneOffset.UTC).toInstant()), event.getDate());
        Assert.assertEquals(Integer.valueOf(2), event.getEpicFactor());
    }

    @Test
    public void testUserIdRequest() {
        ApiResponse<List<OsuApiUser>> response = getOsuApi().getUser(OsuApiGetUserRequest.builder()
                .user("2")
                .build());
        Assert.assertTrue(response.toString(), response.isOk());

        // make sure the requested user is there
        List<OsuApiUser> result = response.getResult();
        Assert.assertEquals(1, result.size());

        // make sure all data was parsed correctly
        OsuApiUser user = response.getResult().get(0);
        Assert.assertEquals(Integer.valueOf(2), user.getUserId());
        Assert.assertEquals("peppy", user.getUserName());
        Assert.assertEquals(Date.from(ZonedDateTime.of(2007, 8, 28, 3, 9, 12, 0, ZoneOffset.UTC).toInstant()), user.getJoinDate());
        Assert.assertTrue(user.getCount300() >= 654376); // non-static value; can only increase
        Assert.assertTrue(user.getCount100() >= 116717); // non-static value; can only increase
        Assert.assertTrue(user.getCount50() >= 24694); // non-static value; can only increase
        Assert.assertTrue(user.getPlayCount() >= 7259); // non-static value; can only increase
        Assert.assertTrue(user.getRankedScore() >= 426845841); // non-static value; can only increase
        Assert.assertTrue(user.getTotalScore() >= 1896249366); // non-static value; can only increase
        Assert.assertTrue(user.getPpRank() >= 0f); // non-static value; can increase or decrease
        Assert.assertTrue(user.getLevel() >= 66.0171f); // non-static value; can only increase
        Assert.assertTrue(user.getPpRaw() >= 0f); // non-static value; can increase or decrease
        Assert.assertTrue(user.getAccuracy() >= 0f); // non-static value; can increase or decrease
        Assert.assertTrue(user.getCountRankSs() >= 0); // non-static value; can increase or decrease
        Assert.assertTrue(user.getCountRankSsh() >= 0); // non-static value; can increase or decrease
        Assert.assertTrue(user.getCountRankS() >= 0); // non-static value; can increase or decrease
        Assert.assertTrue(user.getCountRankSh() >= 0); // non-static value; can increase or decrease
        Assert.assertTrue(user.getCountRankA() >= 0); // non-static value; can increase or decrease
        Assert.assertEquals("AU", user.getCountry());
        Assert.assertTrue(user.getTotalSecondsPlayed() >= 719258); // non-static value; can only increase
        Assert.assertTrue(user.getPpCountryRank() >= 0); // non-static value; can increase or decrease
        Assert.assertNotNull(user.getEvents()); // non-static value; can increase or decrease in size
    }

    @Test
    public void testUserNameRequest() {
        ApiResponse<List<OsuApiUser>> response = getOsuApi().getUser(OsuApiGetUserRequest.builder()
                .user("Geotim")
                .build());
        Assert.assertTrue(response.toString(), response.isOk());

        // make sure the requested user is there
        List<OsuApiUser> result = response.getResult();
        Assert.assertEquals(1, result.size());

        // make sure all data was parsed correctly
        OsuApiUser user = response.getResult().get(0);
        Assert.assertEquals(Integer.valueOf(14182419), user.getUserId());
        Assert.assertEquals("Geotim", user.getUserName());
        Assert.assertEquals(Date.from(ZonedDateTime.of(2019, 3, 29, 20, 55, 12, 0, ZoneOffset.UTC).toInstant()), user.getJoinDate());
        Assert.assertTrue(user.getCount300() >= 1061693); // non-static value; can only increase
        Assert.assertTrue(user.getCount100() >= 115375); // non-static value; can only increase
        Assert.assertTrue(user.getCount50() >= 19199); // non-static value; can only increase
        Assert.assertTrue(user.getPlayCount() >= 6029); // non-static value; can only increase
        Assert.assertTrue(user.getRankedScore() >= 3198609896L); // non-static value; can only increase
        Assert.assertTrue(user.getTotalScore() >= 5897023345L); // non-static value; can only increase
        Assert.assertTrue(user.getPpRank() >= 0); // non-static value; can increase or decrease
        Assert.assertTrue(user.getLevel() >= 93.5604f); // non-static value; can only increase
        Assert.assertTrue(user.getPpRaw() >= 0f); // non-static value; can increase or decrease
        Assert.assertTrue(user.getAccuracy() >= 0f); // non-static value; can increase or decrease
        Assert.assertTrue(user.getCountRankSs() >= 0); // non-static value; can increase or decrease
        Assert.assertTrue(user.getCountRankSsh() >= 0); // non-static value; can increase or decrease
        Assert.assertTrue(user.getCountRankS() >= 0); // non-static value; can increase or decrease
        Assert.assertTrue(user.getCountRankSh() >= 0); // non-static value; can increase or decrease
        Assert.assertTrue(user.getCountRankA() >= 0); // non-static value; can increase or decrease
        Assert.assertEquals("DE", user.getCountry());
        Assert.assertTrue(user.getTotalSecondsPlayed() >= 658852); // non-static value; can only increase
        Assert.assertTrue(user.getPpCountryRank() >= 0); // non-static value; can increase or decrease
        Assert.assertNotNull(user.getEvents()); // non-static value; can increase or decrease in size
    }

}
