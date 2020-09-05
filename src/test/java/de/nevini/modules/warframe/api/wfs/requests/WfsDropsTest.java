package de.nevini.modules.warframe.api.wfs.requests;

import de.nevini.core.api.ApiResponse;
import de.nevini.modules.warframe.api.wfs.model.drops.WfsDrop;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class WfsDropsTest extends WfsApiProvider {

    @Test
    public void testParser() throws IOException {
        WfsDropsRequest request = WfsDropsRequest.builder().build();
        List<WfsDrop> result;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("drops.json")
        )) {
            result = request.parseStream(reader);
        }

        // make sure all data was parsed correctly
        Assert.assertEquals(20106, result.size());
        WfsDrop drop = result.get(0);
        Assert.assertEquals("2,000 Credits Cache", drop.getItem());
        Assert.assertEquals("Mercury/Apollodorus (Survival), Rot A", drop.getPlace());
        Assert.assertEquals("Common", drop.getRarity());
        Assert.assertEquals(50, drop.getChance(), 0);
    }

    @Test
    public void testBlankRequest() {
        ApiResponse<List<WfsDrop>> response = getWfsApi().getDrops(WfsDropsRequest.builder().build());
        Assert.assertTrue(response.toString(), response.isOk());
    }

}
