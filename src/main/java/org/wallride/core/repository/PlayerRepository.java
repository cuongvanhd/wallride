package org.wallride.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.core.domain.Player;

import java.util.Collection;
import java.util.List;

@Repository
@Transactional
public interface PlayerRepository extends JpaRepository<Player, Integer>{

	static final String DEFAULT_SELECT_QUERY =
			"from Player player left join fetch player.country country ";

	@Query("select player.id from Player player ")
	List<Integer> findId();

	@Query(DEFAULT_SELECT_QUERY + "where player.id = :id  ")
	Player findById(@Param("id") Integer id);

	@Query(DEFAULT_SELECT_QUERY + "where player.id in (:ids) ")
	List<Player> findByIdIn(@Param("ids") Collection<Integer> ids);
}
