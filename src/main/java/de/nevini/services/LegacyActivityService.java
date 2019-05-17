package de.nevini.services;

import de.nevini.db.legacy.activity.LegacyActivityRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LegacyActivityService {

    private final ActivityService activityService;
    private final LegacyActivityRepository legacyActivityRepository;

    public LegacyActivityService(
            @Autowired @NonNull ActivityService activityService,
            @Autowired @NonNull LegacyActivityRepository legacyActivityRepository
    ) {
        this.activityService = activityService;
        this.legacyActivityRepository = legacyActivityRepository;
    }

}
