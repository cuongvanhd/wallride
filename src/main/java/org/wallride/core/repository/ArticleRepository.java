package org.wallride.core.repository;

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
import java.util.Map;
import java.util.Set;

@Repository
@Transactional
public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
	
	static final String DEFAULT_SELECT_QUERY = 
			"from Article article " +
			"left join fetch article.bodies bodies " +
			"left join fetch article.seo seo " +
			"left join fetch article.cover cover " +
			"left join fetch article.author author " +
			"left join fetch article.categories category " +
			"left join fetch article.relatedArticles relatedArticles " +
			"left join fetch article.tags tag ";

	@Query("select article.id from Article article order by article.date desc ")
	List<Long> findId();

//	//TODO データ移行後削除
//	@Query("from Article article left join fetch article.links links where article.status = 'PUBLISHED'")
//	Set<Article> findAllArticles();

	@Query(DEFAULT_SELECT_QUERY + "where article.id in (:ids) ")
	List<Article> findByIdIn(@Param("ids") Collection<Long> ids);
	
	@Query(DEFAULT_SELECT_QUERY + "where article.id = :id and article.language = :language ")
	Article findById(@Param("id") Long id, @Param("language") String language);
	
	@Query(DEFAULT_SELECT_QUERY + "where article.id = :id and article.language = :language ")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Article findByIdForUpdate(@Param("id") Long id, @Param("language") String language);

	@Query(DEFAULT_SELECT_QUERY + "where article.code = :code and article.language = :language ")
	Article findByCode(@Param("code") String code, @Param("language") String language);

	@Query("select count(article.id) from Article article where article.language = :language ")
	long count(@Param("language") String language);

	@Query("select count(article.id) from Article article where article.status = :status and article.language = :language ")
	long countByStatus(@Param("status") Post.Status status, @Param("language") String language);

	@Query(
			"select new map(user.id as userId, count(article.id) as count) from Article article " +
			"left join article.author user " +
			"where article.status = :status and article.language = :language " +
			"group by user.id ")
	List<Map<String, Object>> countByAuthorIdGrouped(@Param("status") Post.Status status, @Param("language") String language);

	@Query(
			"select new map(category.id as categoryId, count(article.id) as count) from Article article " +
			"left join article.categories category " +
			"where article.status = :status and article.language = :language " +
			"group by category.id ")
	List<Map<String, Object>> countByCategoryIdGrouped(@Param("status") Post.Status status, @Param("language") String language);
}
