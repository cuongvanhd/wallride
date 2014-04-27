package org.wallride.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.core.domain.Club;

import java.util.Collection;
import java.util.List;

@Repository
@Transactional
public interface ClubRepository extends JpaRepository<Club, Integer>{

	static final String DEFAULT_SELECT_QUERY =
			"from Club club " +
					"left join fetch club.records records ";

	@Query("select club.id from Club club ")
	List<Integer> findId();

	@Query(DEFAULT_SELECT_QUERY + "where club.id = :id  ")
	Club findById(@Param("id") Integer id);

	@Query(DEFAULT_SELECT_QUERY + "where club.id in (:ids) ")
	List<Club> findByIdIn(@Param("ids") Collection<Integer> ids);

	@Query(DEFAULT_SELECT_QUERY + "where records.league.id = :leagueId and club.aliasJa is not null order by club.aliasJa ")
	List<Club> findByLeagueId(@Param("leagueId") Integer leagueId);
}
