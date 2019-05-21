package de.nevini.services;

import de.nevini.db.game.GameData;
import de.nevini.db.ign.IgnData;
import de.nevini.db.ign.IgnId;
import de.nevini.db.ign.IgnRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class IgnService {

    private final IgnRepository ignRepository;

    public IgnService(@Autowired IgnRepository ignRepository) {
        this.ignRepository = ignRepository;
    }

    public String getIgn(@NonNull Member member, @NonNull GameData game) {
        return ignRepository.findById(new IgnId(
                member.getGuild().getIdLong(),
                member.getUser().getIdLong(),
                game.getId()
        )).map(IgnData::getName).orElse(null);
    }

    public List<IgnData> getIgns(@NonNull Member member) {
        return ignRepository.findAllByGuildAndUser(
                member.getGuild().getIdLong(),
                member.getUser().getIdLong()
        );
    }

    public List<IgnData> getIgns(@NonNull Guild guild, @NonNull GameData game) {
        return ignRepository.findAllByGuildAndGame(
                guild.getIdLong(),
                game.getId()
        );
    }

    public synchronized void setIgn(@NonNull Member member, @NonNull GameData game, @NonNull String name) {
        IgnData data = new IgnData(member.getGuild().getIdLong(), member.getUser().getIdLong(), game.getId(), name);
        log.info("Save data: {}", data);
        ignRepository.save(data);
    }

    public List<IgnData> findByIgn(Guild guild, String name) {
        return ignRepository.findAllByGuildAndNameContainsIgnoreCase(
                guild.getIdLong(),
                name
        );
    }

}
