package de.nevini.bot.services.common;

import de.nevini.bot.db.module.ModuleData;
import de.nevini.bot.db.module.ModuleId;
import de.nevini.bot.db.module.ModuleRepository;
import de.nevini.bot.scope.Module;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ModuleService {

    private static final byte FLAG_ACTIVE = 1;
    private static final byte FLAG_INACTIVE = -1;

    private final ModuleRepository moduleRepository;

    public ModuleService(@Autowired ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public boolean isModuleActive(Guild guild, @NonNull Module module) {
        return guild == null || module.isAlwaysEnabled() ||
                moduleRepository.findById(new ModuleId(guild.getIdLong(), module.getName()))
                        .map(data -> data.getFlag() > 0).orElse(module.isEnabledByDefault());
    }

    public synchronized void setModuleActive(@NonNull Guild guild, @NonNull Module module, boolean active) {
        ModuleData data = new ModuleData(guild.getIdLong(), module.getName(), active ? FLAG_ACTIVE : FLAG_INACTIVE);
        log.info("Save data: {}", data);
        moduleRepository.save(data);
    }

}
