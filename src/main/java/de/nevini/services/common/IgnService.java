package de.nevini.services.common;

import de.nevini.jpa.game.GameData;
import de.nevini.jpa.ign.IgnData;
import de.nevini.jpa.ign.IgnId;
import de.nevini.jpa.ign.IgnRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
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

    public String getInGameName(@NonNull Member member, @NonNull GameData game) {
        return Optional.ofNullable(getIgn(member, game)).map(IgnData::getName).orElse(null);
    }

    public IgnData getIgn(@NonNull Member member, @NonNull GameData game) {
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
        return ign.orElse(null);
    }

    public List<IgnData> getIgns(long user, @NonNull GameData game) {
        return ignRepository.findAllByUserAndGame(user, game.getId());
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

    public void setIgn(@NonNull Member member, @NonNull GameData game, @NonNull String name) {
        IgnData data = new IgnData(member.getGuild().getIdLong(), member.getUser().getIdLong(), game.getId(), name);
        log.debug("Save data: {}", data);
        ignRepository.save(data);
    }

    public void setIgn(@NonNull User user, long gameId, @NonNull String name) {
        IgnData data = new IgnData(-1L, user.getIdLong(), gameId, name);
        log.debug("Save data: {}", data);
        ignRepository.save(data);
    }

    public List<IgnData> findByIgn(@NonNull Guild guild, String name) {
        return ignRepository.findAllByGuildInAndNameContainsIgnoreCase(
                new long[]{-1L, guild.getIdLong()},
                name
        );
    }

}
