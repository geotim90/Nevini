package de.nevini.api.osu;

import de.nevini.api.ApiResponse;
import de.nevini.api.osu.model.OsuUser;
import de.nevini.api.osu.requests.OsuUserRequest;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class OsuUserTest extends OsuApiProvider {

    @Test
    public void testUserIdRequest() {
        ApiResponse<List<OsuUser>> response = getOsuApi().getUser(OsuUserRequest.builder()
                .user("2")
                .build());
        Assert.assertTrue(response.toString(), response.isOk());

        // make sure the requested user is there
        List<OsuUser> result = response.getResult();
        Assert.assertEquals(1, result.size());

        // make sure all data was parsed correctly
        OsuUser user = response.getResult().get(0);
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
        ApiResponse<List<OsuUser>> response = getOsuApi().getUser(OsuUserRequest.builder()
                .user("Geotim")
                .build());
        Assert.assertTrue(response.toString(), response.isOk());

        // make sure the requested user is there
        List<OsuUser> result = response.getResult();
        Assert.assertEquals(1, result.size());

        // make sure all data was parsed correctly
        OsuUser user = response.getResult().get(0);
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
