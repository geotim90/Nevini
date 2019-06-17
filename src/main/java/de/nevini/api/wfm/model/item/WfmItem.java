package de.nevini.api.wfm.model.item;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfmItem {

    @SerializedName("id")
    private String id;

    @SerializedName("set_root")
    private Boolean setRoot;

    @SerializedName("mastery_level")
    private Integer masteryLevel;

    @SerializedName("sub_icon")
    private String subIcon;

    @SerializedName("en")
    private WfmItemInformation en;

    @SerializedName("icon_format")
    private String iconFormat;

    @SerializedName("url_name")
    private String urlName;

    @SerializedName("thumb")
    private String thumb;

    @SerializedName("ducats")
    private Integer ducats;

    @SerializedName("icon")
    private String icon;

    @SerializedName("trading_tax")
    private Integer tradingTax;

    @SerializedName("tags")
    private List<String> tags;

}
