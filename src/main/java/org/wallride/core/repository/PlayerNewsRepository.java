package org.wallride.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.core.domain.Player;
import org.wallride.core.domain.PlayerNews;

import java.util.Collection;
import java.util.List;

@Repository
@Transactional
public interface PlayerNewsRepository extends JpaRepository<PlayerNews, Integer>{

	static final String DEFAULT_SELECT_QUERY =
			"from PlayerNews news ";

	@Query("select news.id from PlayerNews news ")
	List<Integer> findId();

	@Query(DEFAULT_SELECT_QUERY + "where news.id = :id  ")
	PlayerNews findById(@Param("id") Integer id);

	@Query(DEFAULT_SELECT_QUERY + "where news.id in (:ids) ")
	List<PlayerNews> findByIdIn(@Param("ids") Collection<Integer> ids);
}
