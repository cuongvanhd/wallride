package org.wallride.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.core.domain.Movement;

import java.util.Collection;
import java.util.List;

@Repository
@Transactional
public interface MovementRepository extends JpaRepository<Movement, Integer>{

	static final String DEFAULT_SELECT_QUERY =
			"from Movement movement " +
					"left join fetch movement.club club " +
					"left join fetch movement.league league " +
					"left join fetch movement.player player " +
					"left join fetch player.country country ";

	@Query("select movement.id from Movement movement ")
	List<Integer> findId();

	@Query(DEFAULT_SELECT_QUERY + "where movement.id = :id  ")
	Movement findById(@Param("id") Integer id);

	@Query(DEFAULT_SELECT_QUERY + "where movement.id in (:ids) ")
	List<Movement> findByIdIn(@Param("ids") Collection<Integer> ids);

	@Query(DEFAULT_SELECT_QUERY + "where movement.player.id = :playerId " +
			"order by case when movement.seasonStartAt > movement.leftAt then movement.leftAt else movement.seasonStartAt end," +
			" movement.leftAt, movement.seasonStartAt, movement.seasonEndAt ")
	List<Movement> findByPlayerId(@Param("playerId") Integer playerId);
}
