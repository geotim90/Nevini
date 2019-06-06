package de.nevini.api.osu.requests;

import de.nevini.api.ApiResponse;
import de.nevini.api.osu.model.OsuMode;
import de.nevini.api.osu.model.OsuReplay;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

public class OsuReplayTest extends OsuApiProvider {

    @Test
    public void testParser() throws IOException {
        OsuReplayRequest request = OsuReplayRequest.builder()
                .mode(OsuMode.STANDARD)
                .beatmapId(1911308)
                .user("7342622")
                .build();
        OsuReplay result;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("get_replay_m_0_b_1911308_u_7342622.json")
        )) {
            result = request.parseStream(reader);
        }

        Assert.assertEquals(41628, result.getContent().length());
        Assert.assertEquals("base64", result.getEncoding());
    }

    @Test
    public void testRequiredParametersRequest() {
        ApiResponse<OsuReplay> response = getOsuApi().getReplay(OsuReplayRequest.builder()
                .mode(OsuMode.STANDARD)
                .beatmapId(1911308)
                .user("7342622")
                .build());
        Assert.assertTrue(response.toString(), response.isOk());

        OsuReplay result = response.getResult();
        Assert.assertEquals(41628, result.getContent().length());
        Assert.assertEquals("base64", result.getEncoding());
    }

}
