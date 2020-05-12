package de.nevini.api.wfs.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder
public class WfsRewards {

    List<WfsReward> rewards;

    Map<String, List<WfsReward>> rotationRewards;

}
