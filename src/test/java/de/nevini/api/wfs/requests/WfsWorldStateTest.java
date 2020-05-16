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
        Assert.assertEquals("2020-05-22T13:00:00.000Z", result.getVoidTrader().getActivation().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        Assert.assertEquals(Boolean.FALSE, result.getVoidTrader().getActive());
        Assert.assertEquals("Orcus Relay (Pluto)", result.getVoidTrader().getLocation());
        // TODO inventory
        Assert.assertEquals("2020-05-24T13:00:00.000Z", result.getVoidTrader().getExpiry().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        // World Cycles
        Assert.assertEquals("30m to Night", result.getCetusCycle().getShortString());
        Assert.assertEquals("18m to Warm", result.getVallisCycle().getShortString());

        // Sortie
        Assert.assertEquals("Lech Kril", result.getSortie().getBoss());
        Assert.assertEquals("2020-05-12T16:00:00.000Z", result.getSortie().getExpiry().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        Assert.assertEquals("Ara (Mars)", result.getSortie().getVariants().get(0).getNode());
        Assert.assertEquals("Rescue", result.getSortie().getVariants().get(0).getMissionType());
        Assert.assertEquals("Environmental Hazard: Radiation Pockets", result.getSortie().getVariants().get(0).getModifier());

        // Arbitration
        Assert.assertEquals("Tycho (Lua)", result.getArbitration().getNode());
        Assert.assertEquals("Survival", result.getArbitration().getType());
        Assert.assertEquals("Corpus", result.getArbitration().getEnemy());
        Assert.assertEquals("2020-05-12T08:04:00Z", result.getArbitration().getExpiry().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        // Fissures
        Assert.assertEquals("Cassini (Saturn)", result.getFissures().get(0).getNode());
        Assert.assertEquals("Capture", result.getFissures().get(0).getMissionType());
        Assert.assertEquals("Grineer", result.getFissures().get(0).getEnemy());
        Assert.assertEquals("Meso", result.getFissures().get(0).getTier());
        Assert.assertEquals(Integer.valueOf(2), result.getFissures().get(0).getTierNum());
        Assert.assertEquals("44m 34s", result.getFissures().get(0).getExpiry().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        Assert.assertEquals("2020-05-12T08:40:04.263Z", result.getFissures().get(0).getExpiry().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }

    @Test
    public void testBlankRequest() {
        ApiResponse<WfsWorldState> response = getWfsApi().getWorldState(WfsWorldStateRequest.builder().build());
        Assert.assertTrue(response.toString(), response.isOk());
    }

}
