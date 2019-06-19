package de.nevini.api.wfs.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder
public class WfsRewards {

    private final List<WfsReward> rewards;

    private final Map<String, List<WfsReward>> rotationRewards;

}
