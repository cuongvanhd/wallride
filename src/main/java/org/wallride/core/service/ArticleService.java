package org.wallride.core.service;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MessageCodesResolver;
import org.wallride.core.domain.*;
import org.wallride.core.repository.ArticleFullTextSearchTerm;
import org.wallride.core.repository.ArticleRepository;
import org.wallride.core.repository.MediaRepository;
import org.wallride.core.repository.TagRepository;
import org.wallride.core.support.AuthorizedUser;
import org.wallride.core.support.Settings;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional(rollbackFor=Exception.class)
public class ArticleService {
	
	@Inject
	private ArticleRepository articleRepository;

	@Inject
	private MediaRepository mediaRepository;

	@Inject
	private TagRepository tagRepository;

	@Inject
	private MessageCodesResolver messageCodesResolver;
	
	@Inject
	private PlatformTransactionManager transactionManager;

	@Inject
	private Settings settings;

	@PersistenceContext
	private EntityManager entityManager;

	private static Logger logger = LoggerFactory.getLogger(ArticleService.class); 

	@CacheEvict(value="articles", allEntries=true)
	public Article createArticle(ArticleCreateRequest request, BindingResult errors, Post.Status status, AuthorizedUser authorizedUser)
			throws BindException, IOException {
		LocalDateTime now = new LocalDateTime();

		String code = (request.getCode() != null) ? request.getCode() : request.getTitle();
		if (!StringUtils.hasText(code)) {
			if (Post.Status.PUBLISHED.equals(status)) {
				errors.rejectValue("code", "NotNull");
			}
		}
		else {
			Article duplicate = articleRepository.findByCode(request.getCode(), request.getLanguage());
			if (duplicate != null) {
				errors.rejectValue("code", "NotDuplicate");
			}
		}

		if (errors.hasErrors()) {
			throw new BindException(errors);
		}

		Article article = new Article();
		Media cover = null;
		if (request.getCoverId() != null) {
			cover = entityManager.getReference(Media.class, request.getCoverId());
		}
		article.setCover(cover);
		article.setTitle(request.getTitle());
		article.setCode(code);

		List<PostBody> bodies = new ArrayList<>();
		if (CollectionUtils.isEmpty(request.getBodies())) {
			errors.rejectValue("bodies", "NotNull");
		}

		if (errors.hasErrors()) {
			throw new BindException(errors);
		}

		article.getBodies().clear();
		for (String requestBody : request.getBodies()) {
			PostBody body = new PostBody();
			body.setBody(requestBody);
			bodies.add(body);
		}
		article.setBodies(bodies);

		User author = entityManager.getReference(User.class, authorizedUser.getId());
//		if (request.getAuthorId() != null) {
//			author = entityManager.getReference(User.class, request.getAuthorId());
//		}
		article.setAuthor(author);

		LocalDateTime date = request.getDate();
		if (Post.Status.PUBLISHED.equals(status)) {
			if (date == null) {
				date = now;
			}
			else if (date.isAfter(now)) {
				status = Post.Status.SCHEDULED;
			}
		}
		article.setDate(date);
		article.setStatus(status);
		article.setLanguage(request.getLanguage());

		article.getCategories().clear();
		for (long categoryId : request.getCategoryIds()) {
			article.getCategories().add(entityManager.getReference(Category.class, categoryId));
		}

		article.getTags().clear();
		String[] tags = org.apache.commons.lang.StringUtils.splitByWholeSeparator(request.getTags(), ",");
		if (!ArrayUtils.isEmpty(tags)) {
			String language = LocaleContextHolder.getLocale().getLanguage();
			for (String tagName : tags) {
				if (tagName.startsWith("_new_")) {
					Tag newTag = new Tag();
					newTag.setName(org.apache.commons.lang.StringUtils.removeStart(tagName, "_new_"));
					newTag.setLanguage(language);
					tagRepository.save(newTag);
					article.getTags().add(newTag);
				}
				else {
					Tag tag = tagRepository.findById(Long.parseLong(tagName), language);
					if (tag != null) {
						article.getTags().add(tag);
					}
				}
			}
		}

		if (article.getSeo() == null) {
			Seo seo = new Seo();
			seo.setKeywords(request.getMetaKeywords());
			seo.setDescription(request.getMetaDescription());
			article.setSeo(seo);
		} else {
			article.getSeo().setKeywords(request.getMetaKeywords());
			article.getSeo().setDescription(request.getMetaDescription());
		}

		List<Media> medias = null;

		if (CollectionUtils.isEmpty(request.getBodies())) {
			medias = new ArrayList<>();
			String mediaUrlPrefix = settings.readSettingAsString(Setting.Key.MEDIA_URL_PREFIX);
			Pattern mediaUrlPattern = Pattern.compile(String.format("%s([0-9a-zA-Z\\-]+)", mediaUrlPrefix));
			for(String body : request.getBodies()) {
				Matcher mediaUrlMatcher = mediaUrlPattern.matcher(body);
				while (mediaUrlMatcher.find()) {
					Media media = mediaRepository.findById(mediaUrlMatcher.group(1));
					medias.add(media);
				}
			}
		}
		article.setMedias(medias);

		article.setCreatedAt(now);
		article.setCreatedBy(authorizedUser.toString());
		article.setUpdatedAt(now);
		article.setUpdatedBy(authorizedUser.toString());

		return articleRepository.save(article);
	}

	@CacheEvict(value="articles", allEntries=true)
	public Article updateArticle(ArticleUpdateRequest request, BindingResult errors, Post.Status status, AuthorizedUser authorizedUser)
			throws BindException, IOException {
		LocalDateTime now = new LocalDateTime();
		Article article = articleRepository.findByIdForUpdate(request.getId(), request.getLanguage());

		String code = (request.getCode() != null) ? request.getCode() : request.getTitle();
		if (!StringUtils.hasText(code)) {
			if (Post.Status.PUBLISHED.equals(status)) {
				errors.rejectValue("code", "NotNull");
			}
		}
		else {
			Article duplicate = articleRepository.findByCode(request.getCode(), request.getLanguage());
			if (duplicate != null && !duplicate.equals(article)) {
				errors.rejectValue("code", "NotDuplicate");
			}
		}

		if (errors.hasErrors()) {
			throw new BindException(errors);
		}

		Media cover = null;
		if (request.getCoverId() != null) {
			cover = entityManager.getReference(Media.class, request.getCoverId());
		}
		article.setCover(cover);
		article.setTitle(request.getTitle());
		article.setCode(code);
		List<PostBody> bodies = new ArrayList<>();
		if (CollectionUtils.isEmpty(request.getBodies())) {
			for (String body : request.getBodies()) {
				if (!StringUtils.hasText(body)) {
					errors.rejectValue("bodies", "NotNull");
				}
			}
		}

		if (errors.hasErrors()) {
			throw new BindException(errors);
		}

		article.getBodies().clear();
		for (String requestBody : request.getBodies()) {
			PostBody body = new PostBody();
			body.setBody(requestBody);
			bodies.add(body);
		}
		article.setBodies(bodies);

//		User author = null;
//		if (request.getAuthorId() != null) {
//			author = entityManager.getReference(User.class, request.getAuthorId());
//		}
//		article.setAuthor(author);

		LocalDateTime date = request.getDate();
		if (Post.Status.PUBLISHED.equals(status)) {
			if (date == null) {
				date = now.withTime(0, 0, 0, 0);
			}
			else if (date.isAfter(now)) {
				status = Post.Status.SCHEDULED;
			}
		}
		article.setDate(date);
		article.setStatus(status);
		article.setLanguage(request.getLanguage());

		article.getCategories().clear();
		for (long categoryId : request.getCategoryIds()) {
			article.getCategories().add(entityManager.getReference(Category.class, categoryId));
		}

		article.getTags().clear();
		String[] tags = org.apache.commons.lang.StringUtils.splitByWholeSeparator(request.getTags(), ",");
		if (!ArrayUtils.isEmpty(tags)) {
			String language = LocaleContextHolder.getLocale().getLanguage();
			for (String tagName : tags) {
				if (tagName.startsWith("_new_")) {
					Tag newTag = new Tag();
					newTag.setName(org.apache.commons.lang.StringUtils.removeStart(tagName, "_new_"));
					newTag.setLanguage(language);
					tagRepository.save(newTag);
					article.getTags().add(newTag);
				}
				else {
					Tag tag = tagRepository.findById(Long.parseLong(tagName), language);
					if (tag != null) {
						article.getTags().add(tag);
					}
				}
			}
		}

		if (article.getSeo() == null) {
			Seo seo = new Seo();
			seo.setKeywords(request.getMetaKeywords());
			seo.setDescription(request.getMetaDescription());
			article.setSeo(seo);
		} else {
			article.getSeo().setKeywords(request.getMetaKeywords());
			article.getSeo().setDescription(request.getMetaDescription());
		}

		List<Media> medias = null;

		if (CollectionUtils.isEmpty(request.getBodies())) {
			medias = new ArrayList<>();
			String mediaUrlPrefix = settings.readSettingAsString(Setting.Key.MEDIA_URL_PREFIX);
			Pattern mediaUrlPattern = Pattern.compile(String.format("%s([0-9a-zA-Z\\-]+)", mediaUrlPrefix));
			for(String body : request.getBodies()) {
				Matcher mediaUrlMatcher = mediaUrlPattern.matcher(body);
				while (mediaUrlMatcher.find()) {
					Media media = mediaRepository.findById(mediaUrlMatcher.group(1));
					medias.add(media);
				}
			}
		}
		article.setMedias(medias);

		article.setUpdatedAt(now);
		article.setUpdatedBy(authorizedUser.toString());

		return articleRepository.save(article);
	}

	@CacheEvict(value="articles", allEntries=true)
	public Article deleteArticle(ArticleDeleteRequest request, BindingResult result) throws BindException {
		Article article = articleRepository.findByIdForUpdate(request.getId(), request.getLanguage());
		articleRepository.delete(article);
		return article;
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	@CacheEvict(value="articles", allEntries=true)
	public List<Article> bulkDeleteArticle(ArticleBulkDeleteRequest bulkDeleteRequest, BindingResult result) {
		List<Article> articles = new ArrayList<>();
		for (long id : bulkDeleteRequest.getIds()) {
			final ArticleDeleteRequest deleteRequest = new ArticleDeleteRequest.Builder()
					.id(id)
					.language(bulkDeleteRequest.getLanguage())
					.build();

			final BeanPropertyBindingResult r = new BeanPropertyBindingResult(deleteRequest, "request");
			r.setMessageCodesResolver(messageCodesResolver);

			TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
			transactionTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
			Article article = null;
			try {
				article = transactionTemplate.execute(new TransactionCallback<Article>() {
					public Article doInTransaction(TransactionStatus status) {
						try {
							return deleteArticle(deleteRequest, r);
						}
						catch (BindException e) {
							throw new RuntimeException(e);
						}
					}
				});
				articles.add(article);
			}
			catch (Exception e) {
				logger.debug("Errors: {}", r);
				result.addAllErrors(r);
			}
		}
		return articles;
	}
	
	public Page<Article> readArticles(ArticleSearchRequest request) {
		Pageable pageable = new PageRequest(0, 10);
		return readArticles(request, pageable);
	}

	public Page<Article> readArticles(ArticleSearchRequest request, Pageable pageable) {
		ArticleFullTextSearchTerm term = request.toFullTextSearchTerm();
		term.setLanguage(LocaleContextHolder.getLocale().getLanguage());
		return articleRepository.findByFullTextSearchTerm(request.toFullTextSearchTerm(), pageable);
	}

	public List<Article> readArticles(Collection<Long> ids) {
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

	@Cacheable(value = "articles", key = "'list.category-code.' + #language + '.' + #code + '.' + #status")
	public SortedSet<Article> readArticlesByCategoryCode(String language, String code, Post.Status status) {
		return readArticlesByCategoryCode(language, code, status, 10); //TODO
	}

	@Cacheable(value = "articles", key = "'list.category-code.' + #language + '.' + #code + '.' + #status + '.' + #size")
	public SortedSet<Article> readArticlesByCategoryCode(String language, String code, Post.Status status, int size) {
		ArticleFullTextSearchTerm term = new ArticleFullTextSearchTerm();
		term.setLanguage(language);
		term.getCategoryCodes().add(code);
		term.setStatus(status);

		Pageable pageable = new PageRequest(0, size);
		Page<Article> page = articleRepository.findByFullTextSearchTerm(term, pageable);
		return new TreeSet<>(page.getContent());
	}

	@Cacheable(value = "articles", key = "'list.latest.' + #language + '.' + #status + '.' + #size")
	public SortedSet<Article> readLatestArticles(String language, Post.Status status, int size) {
		ArticleFullTextSearchTerm term = new ArticleFullTextSearchTerm();
		term.setLanguage(language);
		term.setStatus(status);

		Pageable pageable = new PageRequest(1, size);
		Page<Article> page = articleRepository.findByFullTextSearchTerm(term, pageable);
		return new TreeSet<>(page.getContent());
	}

	public Article readArticleById(long id, String language) {
		return articleRepository.findById(id, language);
	}

	public Article readArticleByCode(String code, String language) {
		return articleRepository.findByCode(code, language);
	}

	public long countArticles(String language) {
		return articleRepository.count(language);
	}

	public long countArticlesByStatus(Post.Status status, String language) {
		return articleRepository.countByStatus(status, language);
	}

	public Map<Long, Long> countArticlesByAuthorIdGrouped(Post.Status status, String language) {
		List<Map<String, Object>> results = articleRepository.countByAuthorIdGrouped(status, language);
		Map<Long, Long> counts = new HashMap<>();
		for (Map<String, Object> row : results) {
			counts.put((Long) row.get("userId"), (Long) row.get("count"));
		}
		return counts;
	}

	public Map<Long, Long> countArticlesByCategoryIdGrouped(Post.Status status, String language) {
		List<Map<String, Object>> results = articleRepository.countByCategoryIdGrouped(status, language);
		Map<Long, Long> counts = new HashMap<>();
		for (Map<String, Object> row : results) {
			counts.put((Long) row.get("categoryId"), (Long) row.get("count"));
		}
		return counts;
	}
}
