package de.nevini.bot.db.feed;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface FeedRepository extends CrudRepository<FeedData, FeedId> {

    Collection<FeedData> findAllByGuildAndId(long guild, long id);

    Collection<FeedData> findAllByGuildAndIdAndChannel(long guild, long id, long channel);

    Collection<FeedData> findAllByTypeAndId(String type, long id);

    void deleteAllByGuildAndType(long idLong, String type);

}
