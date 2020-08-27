package de.nevini.modules.guild.tribute.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TributeMemberRepository extends CrudRepository<TributeMemberData, TributeMemberId> {
}
