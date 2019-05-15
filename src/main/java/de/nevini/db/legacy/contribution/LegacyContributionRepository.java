package de.nevini.db.legacy.contribution;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegacyContributionRepository extends CrudRepository<LegacyContributionData, LegacyContributionId> {

}
