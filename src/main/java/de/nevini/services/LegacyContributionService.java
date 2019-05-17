package de.nevini.services;

import de.nevini.db.legacy.contribution.LegacyContributionData;
import de.nevini.db.legacy.contribution.LegacyContributionId;
import de.nevini.db.legacy.contribution.LegacyContributionRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class LegacyContributionService {

    private static final byte CONTRIBUTED = 1;
    private static final byte NOT_CONTRIBUTED = -1;

    private final LegacyContributionRepository legacyContributionRepository;

    public LegacyContributionService(@Autowired @NonNull LegacyContributionRepository legacyContributionRepository) {
        this.legacyContributionRepository = legacyContributionRepository;
    }

    public boolean getContribution(@NonNull Member member) {
        Optional<LegacyContributionData> data = legacyContributionRepository.findById(new LegacyContributionId(member.getGuild().getIdLong(), member.getUser().getIdLong()));
        return data.map(LegacyContributionData::getFlag).orElse(NOT_CONTRIBUTED) == CONTRIBUTED;
    }

    public synchronized void setContribution(@NonNull Member member, boolean contribution) {
        LegacyContributionData data = new LegacyContributionData(
                member.getGuild().getIdLong(),
                member.getUser().getIdLong(),
                contribution ? CONTRIBUTED : NOT_CONTRIBUTED
        );
        log.info("Save data: {}", data);
        legacyContributionRepository.save(data);
    }

}
