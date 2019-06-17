package de.nevini.api.wfm.requests;

import de.nevini.api.ApiResponse;
import de.nevini.api.wfm.model.items.WfmItemName;
import de.nevini.api.wfm.model.items.WfmItemsResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

public class WfmItemsTest extends WfmApiProvider {

    @Test
    public void testParser() throws IOException {
        WfmItemsRequest request = WfmItemsRequest.builder().build();
        WfmItemsResponse result;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("get_items.json")
        )) {
            result = request.parseStream(reader);
        }

        // make sure all items are present
        Assert.assertEquals(2564, result.getPayload().getItems().getNames().size());

        // make sure all data was parsed correctly
        WfmItemName item = result.getPayload().getItems().getNames().get(0);
        Assert.assertEquals("scindo_prime_handle", item.getUrlName());
        Assert.assertEquals("Scindo Prime Handle", item.getItemName());
        Assert.assertEquals("sub_icons/handle_128x128.png", item.getThumb());
        Assert.assertEquals("54a73e65e779893a797fff5d", item.getId());
    }

    @Test
    public void testBlankRequest() {
        ApiResponse<WfmItemsResponse> response = getWfmApi().getItems(WfmItemsRequest.builder().build());
        Assert.assertTrue(response.toString(), response.isOk());
    }

}
