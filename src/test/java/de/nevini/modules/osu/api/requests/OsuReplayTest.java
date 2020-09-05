package de.nevini.modules.osu.api.requests;

import de.nevini.core.api.ApiResponse;
import de.nevini.modules.osu.api.model.OsuApiReplay;
import de.nevini.modules.osu.model.OsuMode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

public class OsuReplayTest extends OsuApiProvider {

    @Test
    public void testParser() throws IOException {
        OsuApiGetReplayRequest request = OsuApiGetReplayRequest.builder()
                .mode(OsuMode.STANDARD.getId())
                .beatmapId(1911308)
                .user("7342622")
                .build();
        OsuApiReplay result;
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
        ApiResponse<OsuApiReplay> response = getOsuApi().getReplay(OsuApiGetReplayRequest.builder()
                .mode(OsuMode.STANDARD.getId())
                .beatmapId(1911308)
                .user("7342622")
                .build());
        Assert.assertTrue(response.toString(), response.isOk());

        OsuApiReplay result = response.getResult();
        Assert.assertEquals(41628, result.getContent().length());
        Assert.assertEquals("base64", result.getEncoding());
    }

}
