package org.wallride.core.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.wallride.core.domain.*;
import org.wallride.core.repository.*;
import org.wallride.core.support.Paginator;

import javax.inject.Inject;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

@Service
@Transactional(rollbackFor=Exception.class)
public class DefaultModelAttributeService {

	@Inject
	private ArticleRepository articleRepository;

	@Inject
	private ArticleCategoryRepository articleCategoryRepository;

	@Inject
	private PageRepository pageRepository;

	@Inject
	private BannerRepository bannerRepository;

	@Cacheable(value="articles", key="'list.category-code.'+#language+'.'+#code+'.'+#status+'.'+#size")
	public SortedSet<Article> readArticlesByCategoryCode(String language, String code, Post.Status status, int size) {
		ArticleFullTextSearchTerm term = new ArticleFullTextSearchTerm();
		term.setLanguage(language);
		term.getCategoryCodes().add(code);
		term.setStatus(status);
		List<Long> ids = articleRepository.findByFullTextSearchTerm(term);
		if (CollectionUtils.isEmpty(ids)) {
			return new TreeSet<>();
		}
		Paginator<Long> paginator = new Paginator<>(ids, size);
		return new TreeSet<>(articleRepository.findByIdIn(paginator.getElements()));
	}

	@Cacheable(value="articles", key="'category.tree.'+#language")
	public ArticleCategoryTree readArticleCategoryTree(String language) {
		return readArticleCategoryTree(language, false);
	}

	@Cacheable(value="articles", key="'category.tree.'+#language+'.'+#hasArticle")
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

	@Cacheable(value="banners", key="'list.'+#language+'.'+#type")
	public List<Banner> readBanners(Banner.Type type, String language) {
		return bannerRepository.findByType(type, language);
	}
}