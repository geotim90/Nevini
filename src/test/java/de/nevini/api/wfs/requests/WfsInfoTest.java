package de.nevini.api.wfs.requests;

import de.nevini.api.ApiResponse;
import de.nevini.api.wfs.model.WfsInfo;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

public class WfsInfoTest extends WfsApiProvider {

    @Test
    public void testParser() throws IOException {
        WfsInfoRequest request = WfsInfoRequest.builder().build();
        WfsInfo result;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("info.json")
        )) {
            result = request.parseStream(reader);
        }

        // make sure all data was parsed correctly
        Assert.assertEquals("853cb501c5773393d2649c2f8f11faa9", result.getHash());
        Assert.assertEquals(Long.valueOf(1558886414459L), result.getTimestamp());
        Assert.assertEquals(Long.valueOf(1558715889000L), result.getModified());
    }

    @Test
    public void testBlankRequest() {
        ApiResponse<WfsInfo> response = getWfsApi().getInfo(WfsInfoRequest.builder().build());
        Assert.assertTrue(response.toString(), response.isOk());
    }

}
