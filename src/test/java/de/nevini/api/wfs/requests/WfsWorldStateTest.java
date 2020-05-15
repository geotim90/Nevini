package de.nevini.api.wfs.requests;

import de.nevini.api.ApiResponse;
import de.nevini.api.wfs.model.WfsWorldState;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;

public class WfsWorldStateTest extends WfsApiProvider {

    @Test
    public void testParser() throws IOException {
        WfsWorldStateRequest request = WfsWorldStateRequest.builder().build();
        WfsWorldState result;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("worldState.json")
        )) {
            result = request.parseStream(reader);
        }

        // make sure all data was parsed correctly

        // Void Trader
        Assert.assertEquals("10d 5h 4m 30s", result.getVoidTrader().getStartString());
        Assert.assertEquals(Boolean.FALSE, result.getVoidTrader().getActive());
        Assert.assertEquals("Orcus Relay (Pluto)", result.getVoidTrader().getLocation());

        // World Cycles
        Assert.assertEquals("30m to Night", result.getCetusCycle().getShortString());
        Assert.assertEquals("18m to Warm", result.getVallisCycle().getShortString());

        // Sortie
        Assert.assertEquals("Lech Kril", result.getSortie().getBoss());
        Assert.assertEquals("8h 4m 30s", result.getSortie().getEta());
        Assert.assertEquals("Ara (Mars)", result.getSortie().getVariants().get(0).getNode());
        Assert.assertEquals("Rescue", result.getSortie().getVariants().get(0).getMissionType());
        Assert.assertEquals("Environmental Hazard: Radiation Pockets", result.getSortie().getVariants().get(0).getModifier());

        // Arbitration
        Assert.assertEquals("Tycho (Lua)", result.getArbitration().getNode());
        Assert.assertEquals("Survival", result.getArbitration().getType());
        Assert.assertEquals("Corpus", result.getArbitration().getEnemy());
        Assert.assertEquals("2020-05-12T08:04:00Z", result.getArbitration().getExpiry().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }

    @Test
    public void testBlankRequest() {
        ApiResponse<WfsWorldState> response = getWfsApi().getWorldState(WfsWorldStateRequest.builder().build());
        Assert.assertTrue(response.toString(), response.isOk());
    }

}
