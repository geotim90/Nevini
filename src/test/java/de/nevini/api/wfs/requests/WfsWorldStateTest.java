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
        Assert.assertEquals("2020-05-12T07:54:49Z", result.getTimestamp().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        // News
        Assert.assertEquals("Oberon Prime & Nekros Prime Are Back!", result.getNews().get(0).getMessage());
        Assert.assertEquals("http://www.warframe.com/prime-vault?utm_source=in-game&utm_medium=ign", result.getNews().get(0).getLink());
        Assert.assertEquals("2020-02-11T19:13:00Z", result.getNews().get(0).getDate().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        Assert.assertEquals("90d 12h 42m 29s ago", result.getNews().get(0).getEta());
        Assert.assertEquals("[90d 12h 42m 29s ago] [Oberon Prime & Nekros Prime Are Back!](http://www.warframe.com/prime-vault?utm_source=in-game&utm_medium=ign)", result.getNews().get(0).getAsString());

        // Events
        Assert.assertEquals("Thermia Fractures : undefined\nHeat Fissures Event Score : 100\nRewards:\nOpticor Vandal\n\nBattle on Orb Vallis (Venus)\n44% Remaining", result.getEvents().get(0).getAsString());

        // Alerts
        // N/A

        // Sortie
        Assert.assertEquals("Lech Kril", result.getSortie().getBoss());
        Assert.assertEquals("2020-05-12T16:00:00Z", result.getSortie().getExpiry().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        Assert.assertEquals("Ara (Mars)", result.getSortie().getVariants().get(0).getNode());
        Assert.assertEquals("Rescue", result.getSortie().getVariants().get(0).getMissionType());
        Assert.assertEquals("Environmental Hazard: Radiation Pockets", result.getSortie().getVariants().get(0).getModifier());

        // Syndicate Missions
        Assert.assertEquals("2020-05-12T15:59:00Z", result.getSyndicateMissions().get(3).getExpiry().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        Assert.assertEquals("Arbiters of Hexis", result.getSyndicateMissions().get(3).getSyndicate());
        Assert.assertEquals("Gaia (Earth)", result.getSyndicateMissions().get(3).getNodes().get(0));
        Assert.assertEquals("Redirection", result.getSyndicateMissions().get(0).getJobs().get(0).getRewardPool().get(0));
        Assert.assertEquals("Capture Their Leader", result.getSyndicateMissions().get(0).getJobs().get(0).getType());
        Assert.assertEquals(Integer.valueOf(5), result.getSyndicateMissions().get(0).getJobs().get(0).getEnemyLevels().get(0));
        Assert.assertEquals(Integer.valueOf(470), result.getSyndicateMissions().get(0).getJobs().get(0).getStandingStages().get(0));

        // Fissures
        Assert.assertEquals("Cassini (Saturn)", result.getFissures().get(0).getNode());
        Assert.assertEquals("Capture", result.getFissures().get(0).getMissionType());
        Assert.assertEquals("Grineer", result.getFissures().get(0).getEnemy());
        Assert.assertEquals("Meso", result.getFissures().get(0).getTier());
        Assert.assertEquals(Integer.valueOf(2), result.getFissures().get(0).getTierNum());
        Assert.assertEquals("2020-05-12T08:40:04.263Z", result.getFissures().get(0).getExpiry().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        // Global Upgrades

        // Flash Sales

        // Invasions
        Assert.assertEquals("Ludi (Ceres)", result.getInvasions().get(0).getNode());
        Assert.assertEquals("Corpus Siege", result.getInvasions().get(0).getDesc());
        Assert.assertEquals("Corpus", result.getInvasions().get(0).getAttackingFaction());
        Assert.assertEquals("Snipetron Vandal Blueprint", result.getInvasions().get(0).getAttackerReward().getItemString());
        Assert.assertEquals("Grineer", result.getInvasions().get(0).getDefendingFaction());
        Assert.assertEquals("Sheev Heatsink", result.getInvasions().get(0).getDefenderReward().getItemString());

        // Dark Sectors

        // Void Trader
        Assert.assertEquals("2020-05-22T13:00:00Z", result.getVoidTrader().getActivation().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        Assert.assertEquals(Boolean.FALSE, result.getVoidTrader().getActive());
        Assert.assertEquals("Orcus Relay (Pluto)", result.getVoidTrader().getLocation());
        Assert.assertTrue(result.getVoidTrader().getInventory().isEmpty());
        Assert.assertEquals("2020-05-24T13:00:00Z", result.getVoidTrader().getExpiry().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        // Daily Deals
        Assert.assertEquals("Greater Madurai Lens", result.getDailyDeals().get(0).getItem());
        Assert.assertEquals("2020-05-13T07:00:00Z", result.getDailyDeals().get(0).getExpiry().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        Assert.assertEquals("2020-05-12T05:00:00Z", result.getDailyDeals().get(0).getActivation().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        Assert.assertEquals(Integer.valueOf(40), result.getDailyDeals().get(0).getOriginalPrice());
        Assert.assertEquals(Integer.valueOf(32), result.getDailyDeals().get(0).getSalePrice());
        Assert.assertEquals(Integer.valueOf(125), result.getDailyDeals().get(0).getTotal());
        Assert.assertEquals(Integer.valueOf(48), result.getDailyDeals().get(0).getSold());
        Assert.assertEquals("23h 4m 30s", result.getDailyDeals().get(0).getEta());
        Assert.assertEquals(Integer.valueOf(20), result.getDailyDeals().get(0).getDiscount());

        // Simaris

        // Conclave Challenges

        // Persistent Enemies

        // Earth Cycle

        // Cetus Cycle
        Assert.assertEquals("30m to Night", result.getCetusCycle().getShortString());

        // Weekly Challenges

        // Construction Progress

        // Vallis Cycle
        Assert.assertEquals("18m to Warm", result.getVallisCycle().getShortString());

        // Kuva

        // Arbitration
        Assert.assertEquals("Tycho (Lua)", result.getArbitration().getNode());
        Assert.assertEquals("Survival", result.getArbitration().getType());
        Assert.assertEquals("Corpus", result.getArbitration().getEnemy());
        Assert.assertEquals("2020-05-12T08:04:00Z", result.getArbitration().getExpiry().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        // Sentient Outposts

        // Twitter
    }

    @Test
    public void testParser2() throws IOException {
        WfsWorldStateRequest request = WfsWorldStateRequest.builder().build();
        WfsWorldState result;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("worldState2.json")
        )) {
            result = request.parseStream(reader);
        }

        // Void Trader
        Assert.assertEquals("2020-05-22T13:00:00Z", result.getVoidTrader().getActivation().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        Assert.assertEquals(Boolean.TRUE, result.getVoidTrader().getActive());
        Assert.assertEquals("Orcus Relay (Pluto)", result.getVoidTrader().getLocation());
        Assert.assertEquals("Prisma Lotus Emblem", result.getVoidTrader().getInventory().get(0).getItem());
        Assert.assertEquals(Integer.valueOf(50), result.getVoidTrader().getInventory().get(0).getDucats());
        Assert.assertEquals(Integer.valueOf(50000), result.getVoidTrader().getInventory().get(0).getCredits());
        Assert.assertEquals("2020-05-24T13:00:00Z", result.getVoidTrader().getExpiry().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }

    @Test
    public void testBlankRequest() {
        ApiResponse<WfsWorldState> response = getWfsApi().getWorldState(WfsWorldStateRequest.builder().build());
        Assert.assertTrue(response.toString(), response.isOk());
    }

}
