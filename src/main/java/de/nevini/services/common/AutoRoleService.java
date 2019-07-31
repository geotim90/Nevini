package de.nevini.services.common;

import de.nevini.jpa.autorole.AutoRoleData;
import de.nevini.jpa.autorole.AutoRoleId;
import de.nevini.jpa.autorole.AutoRoleRepository;
import de.nevini.jpa.game.GameData;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.VoiceChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

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
        if (repository.existsById(id)) {
            log.info("Delete data: {}", id);
            repository.deleteById(id);
        }
    }

    public Role getJoinRole(@NonNull Guild guild) {
        return repository.findById(new AutoRoleId(guild.getIdLong(), "join", -1L))
                .map(data -> guild.getRoleById(data.getRole())).orElse(null);
    }

    public void setPlayingAutoRole(@NonNull GameData game, @NonNull Role role) {
        AutoRoleData data = new AutoRoleData(role.getGuild().getIdLong(), "playing", game.getId(), role.getIdLong());
        log.info("Save data: {}", data);
        repository.save(data);
    }

    @Transactional
    public void removePlayingAutoRole(@NonNull GameData game, @NonNull Guild guild) {
        AutoRoleId id = new AutoRoleId(guild.getIdLong(), "playing", game.getId());
        if (repository.existsById(id)) {
            log.info("Delete data: {}", id);
            repository.deleteById(id);
        }
    }

    public Collection<AutoRoleData> getPlayingAutoRoles(long game) {
        return repository.findAllByTypeInAndId(new String[]{"playing"}, game);
    }

    public void setPlaysAutoRole(@NonNull GameData game, @NonNull Role role) {
        AutoRoleData data = new AutoRoleData(role.getGuild().getIdLong(), "plays", game.getId(), role.getIdLong());
        log.info("Save data: {}", data);
        repository.save(data);
    }

    @Transactional
    public void removePlaysAutoRole(@NonNull GameData game, @NonNull Guild guild) {
        AutoRoleId id = new AutoRoleId(guild.getIdLong(), "playing", game.getId());
        if (repository.existsById(id)) {
            log.info("Delete data: {}", id);
            repository.deleteById(id);
        }
    }

    public Collection<AutoRoleData> getGameAutoRoles(long game) {
        return repository.findAllByTypeInAndId(new String[]{"playing", "plays"}, game);
    }

    public void setVeteranAutoRole(long days, @NonNull Role role) {
        AutoRoleData data = new AutoRoleData(role.getGuild().getIdLong(), "veteran", days, role.getIdLong());
        log.info("Save data: {}", data);
        repository.save(data);
    }

    @Transactional
    public void removeVeteranAutoRole(long days, @NonNull Guild guild) {
        AutoRoleId id = new AutoRoleId(guild.getIdLong(), "veteran", days);
        if (repository.existsById(id)) {
            log.info("Delete data: {}", id);
            repository.deleteById(id);
        }
    }

    public Collection<AutoRoleData> getVeteranAutoRoles(@NonNull Guild guild) {
        return repository.findAllByGuildAndType(guild.getIdLong(), "veteran");
    }

    public void setVoiceAutoRole(@NonNull VoiceChannel channel, @NonNull Role role) {
        AutoRoleData data = new AutoRoleData(role.getGuild().getIdLong(), "voice", channel.getIdLong(), role.getIdLong());
        log.info("Save data: {}", data);
        repository.save(data);
    }

    @Transactional
    public void removeVoiceAutoRole(@NonNull VoiceChannel channel) {
        AutoRoleId id = new AutoRoleId(channel.getGuild().getIdLong(), "voice", channel.getIdLong());
        if (repository.existsById(id)) {
            log.info("Delete data: {}", id);
            repository.deleteById(id);
        }
    }

    public Role getVoiceRole(@NonNull VoiceChannel channel) {
        return repository.findById(new AutoRoleId(channel.getGuild().getIdLong(), "voice", channel.getIdLong()))
                .map(data -> channel.getGuild().getRoleById(data.getRole())).orElse(null);
    }

}
