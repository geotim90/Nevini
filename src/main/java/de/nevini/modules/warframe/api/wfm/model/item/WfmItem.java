package de.nevini.modules.warframe.api.wfm.model.item;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfmItem {

    @SerializedName("id")
    String id;

    @SerializedName("set_root")
    Boolean setRoot;

    @SerializedName("mastery_level")
    Integer masteryLevel;

    @SerializedName("sub_icon")
    String subIcon;

    @SerializedName("en")
    WfmItemInformation en;

    @SerializedName("icon_format")
    String iconFormat;

    @SerializedName("url_name")
    String urlName;

    @SerializedName("thumb")
    String thumb;

    @SerializedName("ducats")
    Integer ducats;

    @SerializedName("icon")
    String icon;

    @SerializedName("trading_tax")
    Integer tradingTax;

    @SerializedName("tags")
    List<String> tags;

}
