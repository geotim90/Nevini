package de.nevini.modules.warframe.api.wfm.requests;

import de.nevini.core.api.ApiResponse;
import de.nevini.modules.warframe.api.wfm.model.item.WfmItem;
import de.nevini.modules.warframe.api.wfm.model.item.WfmItemResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

public class WfmItemTest extends WfmApiProvider {

    @Test
    public void testParser() throws IOException {
        WfmItemRequest request = WfmItemRequest.builder().itemUrlName("akbronco_prime_blueprint").build();
        WfmItemResponse result;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("get_items_akbronco_prime_blueprint.json")
        )) {
            result = request.parseStream(reader);
        }

        // make sure all three set items are present
        Assert.assertEquals(3, result.getPayload().getItem().getItemsInSet().size());

        // make sure all data was parsed correctly
        WfmItem item = result.getPayload().getItem().getItemsInSet().get(0);
        Assert.assertEquals("54a73e65e779893a797fff22", item.getId());
        Assert.assertEquals(Boolean.FALSE, item.getSetRoot());
        Assert.assertEquals(Integer.valueOf(0), item.getMasteryLevel());
        Assert.assertEquals("sub_icons/blueprint_128x128.png", item.getSubIcon());
        Assert.assertEquals("Akbronco Prime Blueprint", item.getEn().getItemName());
        Assert.assertEquals("<p>The Akbronco Prime lowers  Impact damage to increase  Slash damage, while gaining increased firing rate, critical damage, status chance and magazine size. The Akbronco Prime was added into the game in Update 12.4.</p>", item.getEn().getDescription());
        Assert.assertEquals("http://warframe.wikia.com/wiki/Akbronco_Prime", item.getEn().getWikiLink());
        Assert.assertEquals(9, item.getEn().getDrop().size());
        Assert.assertEquals("Lith S2 Uncommon", item.getEn().getDrop().get(0).getName());
        Assert.assertNull(item.getEn().getDrop().get(0).getLink());
        Assert.assertEquals("land", item.getIconFormat());
        Assert.assertEquals("akbronco_prime_blueprint", item.getUrlName());
        Assert.assertEquals("icons/en/thumbs/Akbronco_Prime_Set.34b5a7f99e5f8c15cc2039a76c725069.128x128.png", item.getThumb());
        Assert.assertEquals(Integer.valueOf(15), item.getDucats());
        Assert.assertEquals("icons/en/Akbronco_Prime_Set.34b5a7f99e5f8c15cc2039a76c725069.png", item.getIcon());
        Assert.assertEquals(Integer.valueOf(2000), item.getTradingTax());
        Assert.assertEquals(2, item.getTags().size());
        Assert.assertEquals("blueprint", item.getTags().get(0));
        Assert.assertEquals("prime", item.getTags().get(1));
    }

    @Test
    public void testItemRequest() {
        ApiResponse<WfmItemResponse> response = getWfmApi().getItem(WfmItemRequest.builder()
                .itemUrlName("akbronco_prime_blueprint").build());
        Assert.assertTrue(response.toString(), response.isOk());
    }

}
