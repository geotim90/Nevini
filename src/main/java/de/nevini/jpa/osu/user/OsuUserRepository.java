package de.nevini.jpa.osu.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OsuUserRepository extends CrudRepository<OsuUserData, OsuUserId> {

    Optional<OsuUserData> findByUserNameAndMode(String userName, int mode);

}
