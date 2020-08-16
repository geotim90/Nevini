package de.nevini.api.wfm.model.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfmOrdersPayload {

    @SerializedName("orders")
    List<WfmOrder> orders;

}
