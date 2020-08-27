package de.nevini.modules.guild.ign.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IgnRepository extends CrudRepository<IgnData, IgnId> {

    List<IgnData> findAllByGuildAndUser(long guild, long user);

    List<IgnData> findAllByGuildAndGame(long guild, long game);

    List<IgnData> findAllByGuildInAndNameContainsIgnoreCase(long[] guild, String name);

    List<IgnData> findAllByUserAndGame(long user, long game);

}
