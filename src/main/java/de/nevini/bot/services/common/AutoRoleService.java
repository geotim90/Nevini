package de.nevini.bot.services.common;

import de.nevini.bot.db.autorole.AutoRoleData;
import de.nevini.bot.db.autorole.AutoRoleId;
import de.nevini.bot.db.autorole.AutoRoleRepository;
import de.nevini.bot.db.game.GameData;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class AutoRoleService {

    private final AutoRoleRepository repository;

    public AutoRoleService(@Autowired AutoRoleRepository repository) {
        this.repository = repository;
    }

    public @NonNull Collection<AutoRoleData> getAutoRoles(@NonNull Guild guild) {
        return repository.findAllByGuild(guild.getIdLong());
    }

    public void setJoinAutoRole(@NonNull Role role) {
        AutoRoleData data = new AutoRoleData(role.getGuild().getIdLong(), "join", -1L, role.getIdLong());
        log.info("Save data: {}", data);
        repository.save(data);
    }

    @Transactional
    public void removeJoinAutoRole(@NonNull Guild guild) {
        AutoRoleId id = new AutoRoleId(guild.getIdLong(), "join", -1L);
        Optional<AutoRoleData> data = repository.findById(id);
        if (data.isPresent()) {
            log.info("Delete data: {}", id);
            repository.deleteById(id);
        }
    }

    public void setPlayingAutoRole(@NonNull GameData game, @NonNull Role role) {
        AutoRoleData data = new AutoRoleData(role.getGuild().getIdLong(), "playing", game.getId(), role.getIdLong());
        log.info("Save data: {}", data);
        repository.save(data);
    }

    @Transactional
    public void removePlayingAutoRole(@NonNull GameData game, @NonNull Guild guild) {
        AutoRoleId id = new AutoRoleId(guild.getIdLong(), "playing", game.getId());
        Optional<AutoRoleData> data = repository.findById(id);
        if (data.isPresent()) {
            log.info("Delete data: {}", id);
            repository.deleteById(id);
        }
    }

    public void setPlaysAutoRole(@NonNull GameData game, @NonNull Role role) {
        AutoRoleData data = new AutoRoleData(role.getGuild().getIdLong(), "plays", game.getId(), role.getIdLong());
        log.info("Save data: {}", data);
        repository.save(data);
    }

    @Transactional
    public void removePlaysAutoRole(@NonNull GameData game, @NonNull Guild guild) {
        AutoRoleId id = new AutoRoleId(guild.getIdLong(), "playing", game.getId());
        Optional<AutoRoleData> data = repository.findById(id);
        if (data.isPresent()) {
            log.info("Delete data: {}", id);
            repository.deleteById(id);
        }
    }

}
