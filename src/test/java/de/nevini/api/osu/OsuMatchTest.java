package de.nevini.api.osu;

import de.nevini.api.ApiResponse;
import de.nevini.api.osu.model.OsuMatch;
import de.nevini.api.osu.requests.OsuMatchRequest;
import org.junit.Assert;
import org.junit.Test;

public class OsuMatchTest extends OsuApiProvider {

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
