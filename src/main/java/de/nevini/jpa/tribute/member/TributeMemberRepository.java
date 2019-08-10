package de.nevini.jpa.tribute.member;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TributeMemberRepository extends CrudRepository<TributeMemberData, TributeMemberId> {

    Collection<TributeMemberData> findAllByGuildAndFlag(long guild, byte flag);

}
