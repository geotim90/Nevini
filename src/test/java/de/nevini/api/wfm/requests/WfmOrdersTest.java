package de.nevini.api.wfm.requests;

import de.nevini.api.ApiResponse;
import de.nevini.api.wfm.model.orders.WfmOrder;
import de.nevini.api.wfm.model.orders.WfmOrdersResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.OffsetDateTime;

public class WfmOrdersTest extends WfmApiProvider {

    @Test
    public void testParser() throws IOException {
        WfmOrdersRequest request = WfmOrdersRequest.builder()
                .itemUrlName("akbronco_prime_blueprint")
                .build();
        WfmOrdersResponse result;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("get_items_akbronco_prime_blueprint_orders.json")
        )) {
            result = request.parseStream(reader);
        }

        // make sure all items are present
        Assert.assertEquals(190, result.getPayload().getOrders().size());

        // make sure all data was parsed correctly
        WfmOrder item = result.getPayload().getOrders().get(0);
        Assert.assertEquals(Boolean.TRUE, item.getVisible());
        Assert.assertEquals(Integer.valueOf(4), item.getQuantity());
        Assert.assertEquals(OffsetDateTime.parse("2017-05-12T11:09:52.000+00:00"), item.getCreationDate());
        Assert.assertEquals("AdamM88", item.getUser().getName());
        Assert.assertEquals(OffsetDateTime.parse("2019-06-17T03:03:33.214+00:00"), item.getUser().getLastSeen());
        Assert.assertEquals(Integer.valueOf(0), item.getUser().getReputationBonus());
        Assert.assertEquals(Integer.valueOf(1), item.getUser().getReputation());
        Assert.assertEquals("en", item.getUser().getRegion());
        Assert.assertEquals("offline", item.getUser().getStatus());
        Assert.assertEquals("55fdcedcb66f832c6ba1eae2", item.getUser().getId());
        Assert.assertNull(item.getUser().getAvatar());
        Assert.assertEquals(OffsetDateTime.parse("2019-06-12T12:45:43.000+00:00"), item.getLastUpdate());
        Assert.assertEquals(Integer.valueOf(5), item.getPlatinum());
        Assert.assertEquals("sell", item.getOrderType());
        Assert.assertEquals("en", item.getRegion());
        Assert.assertEquals("pc", item.getPlatform());
        Assert.assertEquals("591598000f31394bd574951f", item.getId());
    }

    @Test
    public void testItemRequest() {
        ApiResponse<WfmOrdersResponse> response = getWfmApi().getOrders(WfmOrdersRequest.builder()
                .itemUrlName("akbronco_prime_blueprint").build());
        Assert.assertTrue(response.toString(), response.isOk());
    }

}
