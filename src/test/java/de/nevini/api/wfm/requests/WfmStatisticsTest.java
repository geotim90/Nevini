package de.nevini.api.wfm.requests;

import de.nevini.api.ApiResponse;
import de.nevini.api.wfm.model.statistics.WfmStatisticsClosedEntry;
import de.nevini.api.wfm.model.statistics.WfmStatisticsLiveEntry;
import de.nevini.api.wfm.model.statistics.WfmStatisticsResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.OffsetDateTime;

public class WfmStatisticsTest extends WfmApiProvider {

    @Test
    public void testParser() throws IOException {
        WfmStatisticsRequest request = WfmStatisticsRequest.builder()
                .itemUrlName("akbronco_prime_blueprint")
                .build();
        WfmStatisticsResponse result;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("get_items_akbronco_prime_blueprint_statistics.json")
        )) {
            result = request.parseStream(reader);
        }

        // make sure all items are present
        Assert.assertEquals(5, result.getPayload().getClosed().getLast48hours().size());

        // make sure all data was parsed correctly
        WfmStatisticsClosedEntry closed = result.getPayload().getClosed().getLast48hours().get(0);
        Assert.assertEquals(OffsetDateTime.parse("2019-06-15T14:00:00.000+00:00"), closed.getDateTime());
        Assert.assertEquals(Integer.valueOf(1), closed.getVolume());
        Assert.assertEquals(Integer.valueOf(1), closed.getMinPrice());
        Assert.assertEquals(Integer.valueOf(1), closed.getMaxPrice());
        Assert.assertEquals(Integer.valueOf(1), closed.getOpenPrice());
        Assert.assertEquals(Integer.valueOf(1), closed.getClosedPrice());
        Assert.assertEquals(Float.valueOf(1.0f), closed.getAvgPrice());
        Assert.assertEquals(Float.valueOf(1.0f), closed.getWaPrice());
        Assert.assertEquals(Float.valueOf(1f), closed.getMedian());
        Assert.assertEquals(Integer.valueOf(1), closed.getDonchTop());
        Assert.assertEquals(Integer.valueOf(1), closed.getDonchBot());
        Assert.assertEquals("5d05083c3ae3e8002387f862", closed.getId());

        // make sure all items are present
        Assert.assertEquals(70, result.getPayload().getLive().getLast48hours().size());

        // make sure all data was parsed correctly
        WfmStatisticsLiveEntry live = result.getPayload().getLive().getLast48hours().get(0);
        Assert.assertEquals(OffsetDateTime.parse("2019-06-15T07:00:00.000+00:00"), live.getDateTime());
        Assert.assertEquals(Integer.valueOf(1), live.getVolume());
        Assert.assertEquals(Integer.valueOf(1), live.getMinPrice());
        Assert.assertEquals(Integer.valueOf(1), live.getMaxPrice());
        Assert.assertEquals(Float.valueOf(1.0f), live.getAvgPrice());
        Assert.assertEquals(Float.valueOf(1.0f), live.getWaPrice());
        Assert.assertEquals(Float.valueOf(1f), live.getMedian());
        Assert.assertEquals("buy", live.getOrderType());
        Assert.assertEquals(Float.valueOf(2.1f), live.getMovingAvg());
        Assert.assertEquals("5d04a5bd3ae3e8002387999c", live.getId());
    }

    @Test
    public void testItemRequest() {
        ApiResponse<WfmStatisticsResponse> response = getWfmApi().getStatistics(WfmStatisticsRequest.builder()
                .itemUrlName("akbronco_prime_blueprint").build());
        Assert.assertTrue(response.toString(), response.isOk());
    }

}
