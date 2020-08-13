package de.nevini.services.warframe;

import de.nevini.api.wfs.model.weapons.WfsWeapon;
import org.junit.Assert;
import org.junit.Test;

public class WarframeStatsServiceTest {

    @Test
    public void testRepairWeapons() {
        WarframeStatsService service = new WarframeStatsService();
        for (WfsWeapon weapon : service.getWeapons()) {
            Assert.assertNotNull(weapon.getWikiaThumbnail());
            Assert.assertNotNull(weapon.getWikiaUrl());
        }
    }

}
