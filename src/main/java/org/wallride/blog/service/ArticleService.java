package org.wallride.blog.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.blog.web.article.ArticleSearchForm;
import org.wallride.core.domain.Article;
import org.wallride.core.domain.Post;
import org.wallride.core.repository.ArticleFullTextSearchTerm;
import org.wallride.core.repository.ArticleRepository;
import org.wallride.core.support.Paginator;

import javax.inject.Inject;
import java.util.*;

@Service
@Transactional(rollbackFor=Exception.class)
public class ArticleService {

	@Inject
	private ArticleRepository articleRepository;

	@Cacheable(value="articles", key="'id.'+#form+'.PUBLISHED'")
	public Paginator<Long> readArticles(ArticleSearchForm form) {
		ArticleFullTextSearchTerm term = form.toFullTextSearchTerm();
		term.setStatus(Post.Status.PUBLISHED);
		List<Long> ids = articleRepository.findByFullTextSearchTerm(term);
		return new Paginator<Long>(ids, 20);
	}

	@Cacheable(value="articles", key="'list.'+#paginator+'.PUBLISHED'")
	public List<Article> readArticles(Paginator<Long> paginator) {
		if (paginator == null || !paginator.hasElement()) return new ArrayList<Article>();
		return readArticles(paginator.getElements());
	}

	private List<Article> readArticles(Collection<Long> ids) {
		Set<Article> results = new LinkedHashSet<Article>(articleRepository.findByIdIn(ids));
		List<Article> articles = new ArrayList<>();
		for (long id : ids) {
			for (Article article : results) {
				if (id == article.getId()) {
					articles.add(article);
					break;
				}
			}
		}
		return articles;
	}

	@Cacheable(value="articles", key="'code.'+#code+'.'+#language+'.PUBLISHED'")
	public Article readArticle(String code, String language) {
		return articleRepository.findByCode(code, language, Post.Status.PUBLISHED);
	}
}
