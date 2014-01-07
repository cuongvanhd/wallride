package org.wallride.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.core.domain.Article;
import org.wallride.core.domain.Post;

import javax.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

@Repository
@Transactional
public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
	
	static final String DEFAULT_SELECT_QUERY = 
			"from Article article " +
			"left join fetch article.cover cover " +
			"left join fetch article.author author " +
			"left join fetch article.categories category ";

	@Query("select article.id from Article article order by article.date desc ")
	List<Long> findId();
	
	@Query(DEFAULT_SELECT_QUERY + "where article.id in (:ids) ")
	List<Article> findByIdIn(@Param("ids") Collection<Long> ids);
	
	@Query(DEFAULT_SELECT_QUERY + "where article.id = :id and article.language = :language ")
	Article findById(@Param("id") Long id, @Param("language") String language);
	
	@Query(DEFAULT_SELECT_QUERY + "where article.id = :id and article.language = :language ")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Article findByIdForUpdate(@Param("id") Long id, @Param("language") String language);

	@Query(DEFAULT_SELECT_QUERY + "where article.code = :code and article.language = :language ")
	Article findByCode(@Param("code") String code, @Param("language") String language);

	@Query(DEFAULT_SELECT_QUERY + "where article.code = :code and article.language = :language and article.status = :status ")
	Article findByCode(@Param("code") String code, @Param("language") String language, @Param("status") Post.Status status);

	@Query(DEFAULT_SELECT_QUERY + "where category.code = :code ")
	SortedSet<Article> findByCategoryCode(@Param("code") String code);

//	@Query(DEFAULT_SELECT_QUERY + "where article.language = :language and category.code = :code and article.status = :status ")
//	Page<Article> findByCategoryCode(@Param("language") String language, @Param("code") String code, @Param("status") Post.Status status, Pageable pageable);

	@Query("select count(article.id) from Article article where article.language = :language ")
	long count(@Param("language") String language);

	@Query("select count(article.id) from Article article where article.status = :status and article.language = :language ")
	long countByStatus(@Param("status") Post.Status status, @Param("language") String language);
}
