package de.nevini.api.wfs.requests;

import de.nevini.api.ApiResponse;
import de.nevini.api.wfs.model.WfsInfo;
import de.nevini.api.wfs.model.WfsRiven;
import de.nevini.api.wfs.model.WfsRivenData;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class WfsRivensTest extends WfsApiProvider {

    @Test
    public void testParser() throws IOException {
        WfsRivensRequest request = WfsRivensRequest.builder().build();
        Map<String, Map<String, WfsRiven>> result;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("rivens.json")
        )) {
            result = request.parseStream(reader);
        }

        // make sure all data was parsed correctly
        Map<String, WfsRiven> archgunRivenMod = result.get("Archgun Riven Mod");
        WfsRiven veiledArchgunRivenMod = archgunRivenMod.get("Veiled Archgun Riven Mod");
        Assert.assertNull(veiledArchgunRivenMod.getRerolled());
        WfsRivenData data = veiledArchgunRivenMod.getUnrolled();
        Assert.assertEquals("Archgun Riven Mod", data.getItemType());
        Assert.assertEquals("Veiled Archgun Riven Mod", data.getCompatibility());
        Assert.assertEquals(Boolean.FALSE, data.getRerolled());
        Assert.assertEquals(Float.valueOf(24.53f), data.getAvg());
        Assert.assertEquals(Float.valueOf(9.69f), data.getStddev());
        Assert.assertEquals(Integer.valueOf(5), data.getMin());
        Assert.assertEquals(Integer.valueOf(60), data.getMax());
        Assert.assertEquals(Integer.valueOf(1), data.getPop());
        Assert.assertEquals(Float.valueOf(25), data.getMedian());
    }

    @Test
    public void testBlankRequest() {
        ApiResponse<WfsInfo> response = getWfsApi().getInfo(WfsInfoRequest.builder().build());
        Assert.assertTrue(response.toString(), response.isOk());
    }

}