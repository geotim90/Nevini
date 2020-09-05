package de.nevini.modules.warframe.api.wfs.requests;

import de.nevini.core.api.ApiResponse;
import de.nevini.modules.warframe.api.wfs.model.rivens.WfsRiven;
import de.nevini.modules.warframe.api.wfs.model.rivens.WfsRivenData;
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
        ApiResponse<Map<String, Map<String, WfsRiven>>> response = getWfsApi().getRivens(WfsRivensRequest.builder().build());
        Assert.assertTrue(response.toString(), response.isOk());
    }

}
