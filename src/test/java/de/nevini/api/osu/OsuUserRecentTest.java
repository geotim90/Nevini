package de.nevini.api.osu;

import de.nevini.api.ApiResponse;
import de.nevini.api.osu.model.OsuUserRecent;
import de.nevini.api.osu.requests.OsuUserRecentRequest;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class OsuUserRecentTest extends OsuApiProvider {

    @Test
    public void testUserIdRequest() {
        ApiResponse<List<OsuUserRecent>> response = getOsuApi().getUserRecent(OsuUserRecentRequest.builder()
                .user("2")
                .build());
        Assert.assertTrue(response.toString(), response.isOk());

        // unfortunately only goes back 24 hours and is often empty but should never be null
        Assert.assertNotNull(response.getResult());
    }

}
