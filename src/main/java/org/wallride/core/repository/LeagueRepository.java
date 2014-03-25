package org.wallride.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.core.domain.Club;
import org.wallride.core.domain.League;

import java.util.Collection;
import java.util.List;

@Repository
@Transactional
public interface LeagueRepository extends JpaRepository<Club, Integer> {

	static final String DEFAULT_SELECT_QUERY =
			"from League league " +
					"left join fetch league.records records " +
					"left join fetch league.country country ";;

	@Query("select league.id from League league ")
	List<Integer> findId();

	@Query(DEFAULT_SELECT_QUERY + "where league.id = :id  ")
	League findById(@Param("id") Integer id);

	@Query(DEFAULT_SELECT_QUERY + "where league.id in (:ids) ")
	List<League> findByIdIn(@Param("ids") Collection<Integer> ids);

}
