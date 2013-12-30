package org.wallride.core.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.core.domain.*;
import org.wallride.core.repository.ArticleCategoryRepository;
import org.wallride.core.repository.ArticleRepository;
import org.wallride.core.repository.PageRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.SortedSet;

@Service
@Transactional(rollbackFor=Exception.class)
public class DefaultModelAttributeService {

	@Inject
	private ArticleRepository articleRepository;

	@Inject
	private ArticleCategoryRepository articleCategoryRepository;

	@Inject
	private PageRepository pageRepository;
	@Cacheable(value="articles", key="'list.category-code.'+#language+'.'+#code+'.'+#status")
	public SortedSet<Article> readArticlesByCategoryCode(String language, String code, Post.Status status) {
		return articleRepository.findByCategoryCode(language, code, status);
	}

	@Cacheable(value="articles", key="'category.tree.'+#language")
	public ArticleCategoryTree readArticleCategoryTree(String language) {
		return readArticleCategoryTree(language, false);
	}

	@Cacheable(value="articles", key="'category.tree.'+#language+'.has-article'")
	public ArticleCategoryTree readArticleCategoryTree(String language, boolean hasArticle) {
		List<Category> categories = null;
		if (!hasArticle) {
			categories = articleCategoryRepository.findByLanguage(language);
		}
		else {
			categories = articleCategoryRepository.findByLanguageAndHasArticle(language);
		}
		return new ArticleCategoryTree(categories);
	}

	@Cacheable(value="pages", key="'tree.'+#language")
	public PageTree readPageTree(String language) {
		List<Page> pages = pageRepository.findByLanguage(language);
		return new PageTree(pages);
	}

	@Cacheable(value="pages", key="'tree.'+#language+'.'+#status")
	public PageTree readPageTree(String language, Post.Status status) {
		List<Page> pages = pageRepository.findByLanguageAndStatus(language, status);
		return new PageTree(pages);
	}
}