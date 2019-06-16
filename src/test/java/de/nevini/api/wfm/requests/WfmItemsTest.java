package de.nevini.api.wfm.requests;

import de.nevini.api.ApiResponse;
import de.nevini.api.wfm.model.WfmItemsResponse;
import org.junit.Assert;
import org.junit.Test;

public class WfmItemsTest extends WfmApiProvider {

    @Test
    public void testBlankRequest() {
        ApiResponse<WfmItemsResponse> response = getWfmApi().getItems(WfmItemsRequest.builder().build());
        Assert.assertTrue(response.toString(), response.isOk());
    }

}