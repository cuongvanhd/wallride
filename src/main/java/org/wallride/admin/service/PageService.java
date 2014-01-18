package org.wallride.admin.service;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MessageCodesResolver;
import org.wallride.admin.support.AuthorizedUser;
import org.wallride.admin.web.page.*;
import org.wallride.core.domain.*;
import org.wallride.core.repository.MediaRepository;
import org.wallride.core.repository.PageFullTextSearchTerm;
import org.wallride.core.repository.PageRepository;
import org.wallride.core.support.Paginator;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional(rollbackFor=Exception.class)
public class PageService {
	
	@Inject
	private PageRepository pageRepository;

	@Inject
	private MediaRepository mediaRepository;
	
	@Inject
	private MessageCodesResolver messageCodesResolver;
	
	@Inject
	private PlatformTransactionManager transactionManager;

	@Inject
	private Environment environment;

	@PersistenceContext
	private EntityManager entityManager;

	private static Logger logger = LoggerFactory.getLogger(PageService.class);

	@CacheEvict(value="pages", allEntries=true)
	public Page createPage(PageCreateForm form, BindingResult errors, AuthorizedUser authorizedUser) throws BindException {
		LocalDateTime now = new LocalDateTime();

		String code = (form.getCode() != null) ? form.getCode() : form.getTitle();
		if (!StringUtils.hasText(code)) {
			if (Post.Status.PUBLISHED.equals(form.getStatus())) {
				errors.rejectValue("code", "NotNull");
			}
		}
		else {
			Page duplicate = pageRepository.findByCode(form.getCode(), form.getLanguage());
			if (duplicate != null) {
				errors.rejectValue("code", "NotDuplicate");
			}
		}

		if (errors.hasErrors()) {
			throw new BindException(errors);
		}

		Page page = new Page();
		Page parent = (form.getParentId() != null) ? pageRepository.findById(form.getParentId()) : null;
		int rgt = 0;
		if (parent == null) {
			rgt = pageRepository.findMaxRgt();
			rgt++;
		}
		else {
			rgt = parent.getRgt();
			pageRepository.unshiftRgt(rgt);
			pageRepository.unshiftLft(rgt);
		}

//		int depth = (parent == null) ? 1 : parent.getDepth() + 1;
//		int sort = pageRepository.findMaxSortByDepth(depth, form.getLanguage());
//		if (sort == 0 && parent != null) {
//			sort = parent.getSort();
//		}
//		sort++;
//		pageRepository.incrementSortBySortGreaterThanEqual(sort, form.getLanguage());

		page.setParent(parent);
		page.setCode(code);
		page.setLanguage(form.getLanguage());

		Media cover = null;
		if (form.getCoverId() != null) {
			cover = entityManager.getReference(Media.class, form.getCoverId());
		}
		page.setCover(cover);
		page.setTitle(form.getTitle());
		page.setBody(form.getBody());

		User author = entityManager.getReference(User.class, authorizedUser.getId());
//		User author = null;
//		if (form.getAuthorId() != null) {
//			author = entityManager.getReference(User.class, form.getAuthorId());
//		}
		page.setAuthor(author);
		
		LocalDateTime date = form.getDate();
		Post.Status status = form.getStatus();
		if (Post.Status.PUBLISHED.equals(form.getStatus())) {
			if (date == null) {
				date = now.withTime(0, 0, 0, 0);
			}
			else if (date.isAfter(now)) {
				status = Post.Status.SCHEDULED;
			}
		}
		page.setDate(date);
		page.setStatus(status);
		page.setLft(rgt);
		page.setRgt(rgt + 1);
//		page.setDepth(depth);
//		page.setSort(sort);

		List<Media> medias = new ArrayList<>();
		if (StringUtils.hasText(form.getBody())) {
			String mediaUrlPrefix = environment.getRequiredProperty("media.url");
			Pattern mediaUrlPattern = Pattern.compile(String.format("%s([0-9a-zA-Z\\-]+)", mediaUrlPrefix));
			Matcher mediaUrlMatcher = mediaUrlPattern.matcher(form.getBody());
			while (mediaUrlMatcher.find()) {
				Media media = mediaRepository.findById(mediaUrlMatcher.group(1));
				medias.add(media);
			}
		}
		page.setMedias(medias);

		page.setCreatedAt(now);
		page.setCreatedBy(authorizedUser.toString());
		page.setUpdatedAt(now);
		page.setUpdatedBy(authorizedUser.toString());

		return pageRepository.save(page);
	}

	@CacheEvict(value="pages", allEntries=true)
	public Page updatePage(PageEditForm form, BindingResult errors, Post.Status status, AuthorizedUser authorizedUser) throws BindException {
		LocalDateTime now = new LocalDateTime();
		Page page = pageRepository.findByIdForUpdate(form.getId());

		String code = (form.getCode() != null) ? form.getCode() : form.getTitle();
		if (!StringUtils.hasText(code)) {
			if (Post.Status.PUBLISHED.equals(status)) {
				errors.rejectValue("code", "NotNull");
			}
		}
		else {
			Page duplicate = pageRepository.findByCode(form.getCode(), form.getLanguage());
			if (duplicate != null && !duplicate.equals(page)) {
				errors.rejectValue("code", "NotDuplicate");
			}
		}
		if (errors.hasErrors()) {
			throw new BindException(errors);
		}

		Page parent = (form.getParentId() != null) ? entityManager.getReference(Page.class, form.getParentId()) : null;
		if (!(page.getParent() == null && parent == null) && !ObjectUtils.nullSafeEquals(page.getParent(), parent)) {
			pageRepository.shiftLftRgt(page.getLft(), page.getRgt());
			pageRepository.shiftRgt(page.getRgt());
			pageRepository.shiftLft(page.getRgt());

			int rgt = 0;
			if (parent == null) {
				rgt = pageRepository.findMaxRgt();
				rgt++;
			}
			else {
				rgt = parent.getRgt();
				pageRepository.unshiftRgt(rgt);
				pageRepository.unshiftLft(rgt);
			}
			page.setLft(rgt);
			page.setRgt(rgt + 1);
		}

		page.setParent(parent);
		page.setCode(code);
		page.setLanguage(form.getLanguage());

		Media cover = null;
		if (form.getCoverId() != null) {
			cover = entityManager.getReference(Media.class, form.getCoverId());
		}
		page.setCover(cover);
		page.setTitle(form.getTitle());
		page.setBody(form.getBody());

//		User author = null;
//		if (form.getAuthorId() != null) {
//			author = entityManager.getReference(User.class, form.getAuthorId());
//		}
//		page.setAuthor(author);

		LocalDateTime date = form.getDate();
		if (Post.Status.PUBLISHED.equals(status)) {
			if (date == null) {
				date = now.withTime(0, 0, 0, 0);
			}
			else if (date.isAfter(now)) {
				status = Post.Status.SCHEDULED;
			}
		}
		page.setDate(date);
		page.setStatus(status);

		List<Media> medias = new ArrayList<>();
		if (StringUtils.hasText(form.getBody())) {
			String mediaUrlPrefix = environment.getRequiredProperty("media.url");
			Pattern mediaUrlPattern = Pattern.compile(String.format("%s([0-9a-zA-Z\\-]+)", mediaUrlPrefix));
			Matcher mediaUrlMatcher = mediaUrlPattern.matcher(form.getBody());
			while (mediaUrlMatcher.find()) {
				Media media = mediaRepository.findById(mediaUrlMatcher.group(1));
				medias.add(media);
			}
		}
		page.setMedias(medias);

		page.setUpdatedAt(now);
		page.setUpdatedBy(authorizedUser.toString());

		return pageRepository.save(page);
	}

	@CacheEvict(value="pages", allEntries=true)
	public void updatePageHierarchy(List<Map<String, Object>> data) {
		for (int i = 0; i < data.size(); i++) {
			Map<String, Object> map = data.get(i);
			if (map.get("item_id") != null) {
				Page page = pageRepository.findByIdForUpdate(Long.parseLong((String) map.get("item_id")));
				if (page != null) {
					Page parent = null;
					if (map.get("parent_id") != null) {
						parent = pageRepository.findById(Long.parseLong((String) map.get("parent_id")));
					}
					page.setParent(parent);
					page.setLft(((int) map.get("left")) - 1);
					page.setRgt(((int) map.get("right")) - 1);
//					page.setDepth((int) map.get("depth"));
//					page.setSort(i);
					pageRepository.save(page);
				}
			}
		}
	}

	@CacheEvict(value="pages", allEntries=true)
	public Page deletePage(PageDeleteForm form, BindingResult result) throws BindException {
		Page page = pageRepository.findByIdForUpdate(form.getId());
		Page parent = page.getParent();
		for (Page child : page.getChildren()) {
			child.setParent(parent);
			pageRepository.saveAndFlush(child);
		}
		page.getChildren().clear();
		pageRepository.saveAndFlush(page);
		pageRepository.delete(page);

		pageRepository.shiftLftRgt(page.getLft(), page.getRgt());
		pageRepository.shiftRgt(page.getRgt());
		pageRepository.shiftLft(page.getRgt());

		return page;
	}

	@CacheEvict(value="pages", allEntries=true)
	public Page deletePage(long id, String language) {
		Page page = pageRepository.findByIdForUpdate(id);
		Page parent = page.getParent();
		for (Page child : page.getChildren()) {
			child.setParent(parent);
			pageRepository.saveAndFlush(child);
		}
		page.getChildren().clear();
		pageRepository.saveAndFlush(page);
		pageRepository.delete(page);

		pageRepository.shiftLftRgt(page.getLft(), page.getRgt());
		pageRepository.shiftRgt(page.getRgt());
		pageRepository.shiftLft(page.getRgt());

		return page;
	}

	@CacheEvict(value="pages", allEntries=true)
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Page> bulkDeletePage(PageBulkDeleteForm bulkDeleteForm, BindingResult result) {
		List<Page> pages = new ArrayList<>();
		for (long id : bulkDeleteForm.getIds()) {
			final PageDeleteForm deleteForm = new PageDeleteForm();
			deleteForm.setId(id);
			deleteForm.setConfirmed(bulkDeleteForm.isConfirmed());
			deleteForm.setLanguage(bulkDeleteForm.getLanguage());

			final BeanPropertyBindingResult r = new BeanPropertyBindingResult(deleteForm, "form");
			r.setMessageCodesResolver(messageCodesResolver);

			TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
			transactionTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
			Page page = null;
			try {
				page = transactionTemplate.execute(new TransactionCallback<Page>() {
					public Page doInTransaction(TransactionStatus status) {
						try {
							return deletePage(deleteForm, r);
						}
						catch (BindException e) {
							throw new RuntimeException(e);
						}
					}
				});
				pages.add(page);
			}
			catch (Exception e) {
				logger.debug("Errors: {}", r);
				result.addAllErrors(r);
			}
		}
		return pages;
	}
	
	public Paginator<Long> readPages(PageSearchForm form, int perPage) {
		if (form.isEmpty()) {
			return readAllPages();
		}
		PageFullTextSearchTerm term = form.toFullTextSearchTerm();
		term.setLanguage(LocaleContextHolder.getLocale().getLanguage());
		List<Long> ids = pageRepository.findByFullTextSearchTerm(form.toFullTextSearchTerm());
		return new Paginator<Long>(ids, perPage);
	}
	
	private Paginator<Long> readAllPages() {
		List<Long> results = pageRepository.findId();
		if (results.isEmpty()) {
			return Paginator.getEmptyPaginator();
		}
		else {
			return new Paginator<Long>(results);
		}
	}
	
	public List<Page> readPages(Paginator<Long> paginator) {
		if (paginator == null || !paginator.hasElement()) return new ArrayList<Page>();
		return readPages(paginator.getElements());
	}
	
	public List<Page> readPages(Collection<Long> ids) {
		Set<Page> results = new LinkedHashSet<Page>(pageRepository.findByIdIn(ids));
		List<Page> pages = new ArrayList<>();
		for (long id : ids) {
			for (Page page : results) {
				if (id == page.getId()) {
					pages.add(page);
					break;
				}
			}
		}
		return pages;
	}
	
	public Page readPageById(long id) {
		return pageRepository.findById(id);
	}
	
	public PageTree readPageTree(String language) {
		List<Page> pages = pageRepository.findByLanguage(language);
		return new PageTree(pages);
	}

	public long countPages(String language) {
		return pageRepository.count(language);
	}

	public long countPagesByStatus(Post.Status status, String language) {
		return pageRepository.countByStatus(status, language);
	}
}
