package de.nevini.db.feed;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends CrudRepository<FeedData, FeedId> {

    List<FeedData> findAllByType(String type);

}
