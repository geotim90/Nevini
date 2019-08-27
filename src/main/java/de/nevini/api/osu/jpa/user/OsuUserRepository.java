package de.nevini.api.osu.jpa.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OsuUserRepository extends CrudRepository<OsuUserData, OsuUserId> {

}
