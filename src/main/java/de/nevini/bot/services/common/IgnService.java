package de.nevini.bot.services.common;

import de.nevini.bot.db.game.GameData;
import de.nevini.bot.db.ign.IgnData;
import de.nevini.bot.db.ign.IgnId;
import de.nevini.bot.db.ign.IgnRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class IgnService {

    private final IgnRepository ignRepository;

    public IgnService(@Autowired IgnRepository ignRepository) {
        this.ignRepository = ignRepository;
    }

    public String getIgn(@NonNull Member member, @NonNull GameData game) {
        Optional<IgnData> ign = ignRepository.findById(new IgnId(
                member.getGuild().getIdLong(),
                member.getUser().getIdLong(),
                game.getId()
        ));
        if (!ign.isPresent()) {
            ign = ignRepository.findById(new IgnId(
                    -1L,
                    member.getUser().getIdLong(),
                    game.getId()
            ));
        }
        return ign.map(IgnData::getName).orElse(null);
    }

    public List<IgnData> getIgns(@NonNull Member member) {
        return ListUtils.sum(
                ignRepository.findAllByGuildAndUser(
                        -1L,
                        member.getUser().getIdLong()),
                ignRepository.findAllByGuildAndUser(
                        member.getGuild().getIdLong(),
                        member.getUser().getIdLong())
        );
    }

    public List<IgnData> getIgns(@NonNull Guild guild, @NonNull GameData game) {
        return ListUtils.sum(
                ignRepository.findAllByGuildAndGame(
                        -1L,
                        game.getId()),
                ignRepository.findAllByGuildAndGame(
                        guild.getIdLong(),
                        game.getId())
        );
    }

    public synchronized void setIgn(@NonNull Member member, @NonNull GameData game, @NonNull String name) {
        IgnData data = new IgnData(member.getGuild().getIdLong(), member.getUser().getIdLong(), game.getId(), name);
        log.info("Save data: {}", data);
        ignRepository.save(data);
    }

    public synchronized void setIgn(@NonNull User user, long gameId, @NonNull String name) {
        IgnData data = new IgnData(-1L, user.getIdLong(), gameId, name);
        log.info("Save data: {}", data);
        ignRepository.save(data);
    }

    public List<IgnData> findByIgn(@NonNull Guild guild, String name) {
        return ignRepository.findAllByGuildInAndNameContainsIgnoreCase(
                new long[]{-1L, guild.getIdLong()},
                name
        );
    }

}