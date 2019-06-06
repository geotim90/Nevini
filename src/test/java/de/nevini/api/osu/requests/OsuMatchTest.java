package de.nevini.api.osu.requests;

import de.nevini.api.ApiResponse;
import de.nevini.api.osu.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

public class OsuMatchTest extends OsuApiProvider {

    @Test
    public void testParser() throws IOException {
        OsuMatchRequest request = OsuMatchRequest.builder()
                .matchId(51405592)
                .build();
        OsuMatch result;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("get_match_mp_51405592.json")
        )) {
            result = request.parseStream(reader);
        }

        OsuMatchData match = result.getMatch();
        Assert.assertEquals(Integer.valueOf(51405592), match.getMatchId());
        Assert.assertEquals("HeartPower's game", match.getName());
        Assert.assertEquals(Date.from(ZonedDateTime.of(2019, 4, 30, 7, 27, 8, 0, ZoneOffset.UTC).toInstant()), match.getStartTime());
        Assert.assertEquals(Date.from(ZonedDateTime.of(2019, 4, 30, 7, 31, 10, 0, ZoneOffset.UTC).toInstant()), match.getEndTime());

        OsuGame game = result.getGames().get(0);
        Assert.assertEquals(Integer.valueOf(268249987), game.getGameId());
        Assert.assertEquals(Date.from(ZonedDateTime.of(2019, 4, 30, 7, 27, 22, 0, ZoneOffset.UTC).toInstant()), game.getStartTime());
        Assert.assertEquals(Date.from(ZonedDateTime.of(2019, 4, 30, 7, 31, 0, 0, ZoneOffset.UTC).toInstant()), game.getEndTime());
        Assert.assertEquals(Integer.valueOf(484611), game.getBeatmapId());
        Assert.assertEquals(OsuMode.STANDARD, game.getPlayMode());
        Assert.assertEquals(OsuMatchType.UNKNOWN, game.getMatchType());
        Assert.assertEquals(OsuScoringType.SCORE, game.getScoringType());
        Assert.assertEquals(OsuTeamType.HEAD_TO_HEAD, game.getTeamType());
        Assert.assertArrayEquals(new OsuMod[0], game.getMods());

        OsuGameScore score = game.getScores().get(0);
        Assert.assertEquals(Integer.valueOf(0), score.getSlot());
        Assert.assertEquals(OsuTeam.NONE, score.getTeam());
        Assert.assertEquals(Integer.valueOf(11469987), score.getUserId());
        Assert.assertEquals(Integer.valueOf(122494), score.getScore());
        Assert.assertEquals(Integer.valueOf(49), score.getMaxCombo());
        Assert.assertNull(score.getRank()); // always "0"
        Assert.assertEquals(Integer.valueOf(7), score.getCount50());
        Assert.assertEquals(Integer.valueOf(36), score.getCount100());
        Assert.assertEquals(Integer.valueOf(145), score.getCount300());
        Assert.assertEquals(Integer.valueOf(8), score.getCountMiss());
        Assert.assertEquals(Integer.valueOf(32), score.getCountGeki());
        Assert.assertEquals(Integer.valueOf(21), score.getCountKatu());
        Assert.assertEquals(Boolean.FALSE, score.getPerfect());
        Assert.assertEquals(Boolean.TRUE, score.getPass());
        Assert.assertNull(score.getMods());
    }

    @Test
    public void testMatchIdRequest() {
        ApiResponse<OsuMatch> response = getOsuApi().getMatch(OsuMatchRequest.builder()
                .matchId(1936471)
                .build());
        Assert.assertTrue(response.toString(), response.isOk());
        Assert.assertNotNull(response.getResult());
        Assert.assertNotNull(response.getResult().getGames());
    }

}
