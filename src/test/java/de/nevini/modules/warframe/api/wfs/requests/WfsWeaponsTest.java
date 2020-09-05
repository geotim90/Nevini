package de.nevini.modules.warframe.api.wfs.requests;

import de.nevini.core.api.ApiResponse;
import de.nevini.modules.warframe.api.wfs.model.weapons.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class WfsWeaponsTest extends WfsApiProvider {

    @Test
    public void testParser() throws IOException {
        WfsWeaponsRequest request = WfsWeaponsRequest.builder().build();
        List<WfsWeapon> result;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("weapons.json")
        )) {
            result = request.parseStream(reader);
        }

        // make sure all data was parsed correctly
        WfsWeapon weapon = result.get(0);
        Assert.assertEquals("Acceltra", weapon.getName());
        Assert.assertEquals("/Lotus/Weapons/Tenno/LongGuns/SapientPrimary/SapientPrimaryWeapon", weapon.getUniqueName());
        Assert.assertArrayEquals(new Float[]{26f, 35.200001f, 8.7999992f,
                0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f}, weapon.getDamagePerShot());
        Assert.assertEquals(70, weapon.getTotalDamage(), 0);
        Assert.assertEquals("Using a barrage of rapid-fire plasma rockets, Gauss’ signature weapon lays down a path of destruction. Reloads are faster while sprinting, even more so in Gauss’ hands. For safety, rockets arm after traveling a safe distance.", weapon.getDescription());
        Assert.assertEquals(0.31999999f, weapon.getCriticalChance(), 0);
        Assert.assertEquals(2.8f, weapon.getCriticalMultiplier(), 0);
        Assert.assertEquals(0.060000002f, weapon.getProcChance(), 0);
        Assert.assertEquals(12.000001f, weapon.getFireRate(), 0);
        Assert.assertEquals(8, weapon.getMasteryReq(), 0);
        Assert.assertEquals("LongGuns", weapon.getProductCategory());
        Assert.assertEquals(1, weapon.getSlot(), 0);
        Assert.assertEquals(23.529411f, weapon.getAccuracy(), 0);
        Assert.assertEquals(0.64999998f, weapon.getOmegaAttenuation(), 0);
        Assert.assertEquals("Alarming", weapon.getNoise());
        Assert.assertEquals("Auto", weapon.getTrigger());
        Assert.assertEquals(48, weapon.getMagazineSize(), 0);
        Assert.assertEquals(2, weapon.getReloadTime(), 0);
        Assert.assertEquals(1, weapon.getMultishot(), 0);
        Assert.assertEquals(25000, weapon.getBuildPrice(), 0);
        Assert.assertEquals(86400, weapon.getBuildTime(), 0);
        Assert.assertEquals(35, weapon.getSkipBuildTimePrice(), 0);
        Assert.assertEquals(1, weapon.getBuildQuantity(), 0);
        Assert.assertTrue(weapon.getConsumeOnBuild());

        WfsComponent component = weapon.getComponents().get(0);
        Assert.assertEquals("/Lotus/Types/Recipes/Weapons/SapientPrimaryBlueprint", component.getUniqueName());
        Assert.assertEquals("Blueprint", component.getName());
        Assert.assertEquals("Using a barrage of rapid-fire plasma rockets, Gauss’ signature weapon lays down a path of destruction. Reloads are faster while sprinting, even more so in Gauss’ hands. For safety, rockets arm after traveling a safe distance.", component.getDescription());
        Assert.assertEquals(1, component.getItemCount(), 0);
        Assert.assertEquals("blueprint.png", component.getImageName());
        Assert.assertFalse(component.getTradable());
        WfsDrop drop = component.getDrops().get(0);
        Assert.assertEquals("Demolisher Charger nce: 2.50 nce: 2.50", drop.getLocation());
        Assert.assertEquals("Enemy Blueprint Tables", drop.getType());
        Assert.assertEquals("Common", drop.getRarity());
        Assert.assertEquals(0.5f, drop.getChance(), 0);

        Assert.assertEquals("Rifle", weapon.getType());
        Assert.assertEquals("acceltra.png", weapon.getImageName());
        Assert.assertEquals("Primary", weapon.getCategory());
        Assert.assertFalse(weapon.getTradable());

        WfsPatchlog patchlog = weapon.getPatchlogs().get(0);
        Assert.assertEquals("Operation: Scarlet Spear: TennoGen 27.3.8 + 27.3.8.1 + 27.3.8.2", patchlog.getName());
        Assert.assertEquals("2020-04-02T16:33:08Z", patchlog.getDate().toString());
        Assert.assertEquals("https://forums.warframe.com/topic/1181462-operation-scarlet-spear-tennogen-2738-27381-27382/", patchlog.getUrl());
        Assert.assertEquals("http://n9e5v4d8.ssl.hwcdn.net/uploads/76e24c6720e1e3270f3d504ad1192e48.png", patchlog.getImgUrl());
        Assert.assertEquals("", patchlog.getAdditions());
        Assert.assertEquals("The Pox, Acceltra, and Shedu have been given explosion FX tweaks for visual and performance improvement.", patchlog.getChanges());
        Assert.assertEquals("", patchlog.getFixes());

        Assert.assertEquals(96, weapon.getAmmo(), 0);

        WfsAreaAttack areaAttack = weapon.getAreaAttack();
        Assert.assertEquals("Rocket Explosion", areaAttack.getName());
        WfsFalloff falloff = areaAttack.getFalloff();
        Assert.assertEquals(0, falloff.getStart(), 0);
        Assert.assertEquals(4, falloff.getEnd(), 0);
        Assert.assertEquals(0.5f, falloff.getReduction(), 0);
        Assert.assertEquals(8.8f, areaAttack.getSlash(), 0);
        Assert.assertEquals(35.2f, areaAttack.getPuncture(), 0);
        Assert.assertNull(areaAttack.getImpact());

        Assert.assertEquals("35.0", weapon.getDamage());
        Assert.assertEquals(35, weapon.getDamageTypes().get("impact"), 0);
        Assert.assertEquals(70, weapon.getFlight(), 0);
        Assert.assertEquals(240, weapon.getMarketCost(), 0);
        Assert.assertArrayEquals(new String[]{"Naramon"}, weapon.getPolarities());
        Assert.assertNull(weapon.getStancePolarity());
        Assert.assertEquals("Projectile", weapon.getProjectile());
        Assert.assertArrayEquals(new String[]{"Tenno"}, weapon.getTags());
        Assert.assertEquals("https://vignette.wikia.nocookie.net/warframe/images/1/19/Acceltra.png/revision/latest?cb=20190831102245", weapon.getWikiaThumbnail());
        Assert.assertEquals("http://warframe.fandom.com/wiki/Acceltra", weapon.getWikiaUrl());
        Assert.assertEquals(1, weapon.getDisposition(), 0);
    }

    @Test
    public void testBlankRequest() {
        ApiResponse<List<WfsWeapon>> response = getWfsApi().getWeapons(WfsWeaponsRequest.builder().build());
        Assert.assertTrue(response.toString(), response.isOk());
    }

}
