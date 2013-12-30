package org.wallride.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.core.domain.Banner;

import javax.persistence.LockModeType;
import java.util.Collection;
import java.util.List;

@Repository
@Transactional
public interface BannerRepository extends JpaRepository<Banner, Long> {
	
	static final String DEFAULT_SELECT_QUERY = 
			"from Banner banner " +
			"left join fetch banner.image image ";

	@Query("select banner.id from Banner banner order by banner.sort desc ")
	List<Long> findId();
	
	@Query(DEFAULT_SELECT_QUERY + "where banner.id in (:ids) ")
	List<Banner> findByIdIn(@Param("ids") Collection<Long> ids);

	@Query(DEFAULT_SELECT_QUERY + "where banner.id = :id and banner.language = :language ")
	Banner findById(@Param("id") Long id, @Param("language") String language);

	@Query(DEFAULT_SELECT_QUERY + "where banner.type = :type and banner.language = :language order by banner.sort")
	List<Banner> findByType(@Param("type") Banner.Type type, @Param("language") String language);

	@Query(DEFAULT_SELECT_QUERY + "where banner.id = :id and banner.language = :language")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Banner findByIdForUpdate(@Param("id") Long id, @Param("language") String language);

	@Query("select count(banner.id) from Banner banner where banner.type = :type and banner.language = :language ")
	long countByType(@Param("type") Banner.Type type, @Param("language") String language);

	@Query("select coalesce(max(banner.sort), 0) from Banner banner where banner.type = :type and banner.language = :language ")
	int findMaxSortByType(@Param("type") Banner.Type type, @Param("language") String language);

	@Modifying
	@Query("update Banner set sort = sort - 1 where sort > :sort and language = :language ")
	void decrementSortBySortGreaterThan(@Param("sort") int sort, @Param("language") String language);

}
