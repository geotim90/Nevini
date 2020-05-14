package de.nevini.api.wfs.requests;

import de.nevini.api.ApiResponse;
import de.nevini.api.wfs.model.WfsInfo;
import de.nevini.api.wfs.model.WfsRiven;
import de.nevini.api.wfs.model.WfsRivens;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class WfsRivensTest extends WfsApiProvider {

    @Test
    public void testParser() throws IOException {
        WfsRivensRequest request = WfsRivensRequest.builder().build();
        Map<String, Map<String, WfsRivens>> result;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("rivens.json")
        )) {
            result = request.parseStream(reader);
        }

        // make sure all data was parsed correctly
        Map<String, WfsRivens> archgunRivenMod = result.get("Archgun Riven Mod");
        WfsRivens veiledArchgunRivenMod = archgunRivenMod.get("Veiled Archgun Riven Mod");
        Assert.assertNull(veiledArchgunRivenMod.getRerolled());
        WfsRiven riven = veiledArchgunRivenMod.getUnrolled();
        Assert.assertEquals("Archgun Riven Mod", riven.getItemType());
        Assert.assertEquals("Veiled Archgun Riven Mod", riven.getCompatibility());
        Assert.assertEquals(Boolean.FALSE, riven.getRerolled());
        Assert.assertEquals(Float.valueOf(24.53f), riven.getAvg());
        Assert.assertEquals(Float.valueOf(9.69f), riven.getStddev());
        Assert.assertEquals(Integer.valueOf(5), riven.getMin());
        Assert.assertEquals(Integer.valueOf(60), riven.getMax());
        Assert.assertEquals(Integer.valueOf(1), riven.getPop());
        Assert.assertEquals(Float.valueOf(25), riven.getMedian());
    }

    @Test
    public void testBlankRequest() {
        ApiResponse<WfsInfo> response = getWfsApi().getInfo(WfsInfoRequest.builder().build());
        Assert.assertTrue(response.toString(), response.isOk());
    }

}
