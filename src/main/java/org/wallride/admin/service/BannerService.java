package org.wallride.admin.service;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.wallride.admin.support.AuthorizedUser;
import org.wallride.admin.web.banner.BannerCreateForm;
import org.wallride.admin.web.banner.BannerEditForm;
import org.wallride.core.domain.Banner;
import org.wallride.core.domain.Media;
import org.wallride.core.repository.BannerRepository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor=Exception.class)
public class BannerService {
	
	@Inject
	private BannerRepository bannerRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private static Logger logger = LoggerFactory.getLogger(BannerService.class);

	@CacheEvict(value="banners", allEntries=true)
	public Banner createBanner(BannerCreateForm form, BindingResult errors, AuthorizedUser authorizedUser) throws BindException {
		LocalDateTime now = new LocalDateTime();

		if (errors.hasErrors()) {
			throw new BindException(errors);
		}

		Banner banner = new Banner();
		banner.setLanguage(form.getLanguage());
		Media image = null;
		if (form.getImageId() != null) {
			image = entityManager.getReference(Media.class, form.getImageId());
		}
		banner.setImage(image);
		banner.setTitle(form.getTitle());
		banner.setLink(form.getLink());
		int sort = bannerRepository.findMaxSortByType(form.getType(), form.getLanguage());
		sort++;
		banner.setSort(sort);

		banner.setType(form.getType());

		banner.setCreatedAt(now);
		banner.setCreatedBy(authorizedUser.toString());
		banner.setUpdatedAt(now);
		banner.setUpdatedBy(authorizedUser.toString());

		return bannerRepository.save(banner);
	}

	@CacheEvict(value="banners", allEntries=true)
	public Banner updateBanner(BannerEditForm form, BindingResult errors, AuthorizedUser authorizedUser) throws BindException {
		LocalDateTime now = new LocalDateTime();
		Banner banner = bannerRepository.findByIdForUpdate(form.getId(), form.getLanguage());
		banner.setLanguage(form.getLanguage());
		Media image = null;
		if (form.getImageId() != null) {
			image = entityManager.getReference(Media.class, form.getImageId());
		}
		banner.setImage(image);
		banner.setTitle(form.getTitle());
		banner.setLink(form.getLink());
		banner.setType(form.getType());

		banner.setUpdatedAt(now);
		banner.setUpdatedBy(authorizedUser.toString());

		return bannerRepository.save(banner);
	}
//TODO
/*
	@CacheEvict(value="banners", allEntries=true)
	public Banner deleteBanner(BannerDeleteForm form, BindingResult result) throws BindException {
		Banner banner = bannerRepository.findByIdForUpdate(form.getId(), form.getLanguage());
		bannerRepository.delete(banner);
		return banner;
	}
	
	public Paginator<Long> readBanners(BannerSearchForm form, int perPage) {
		if (form.isEmpty()) {
			return readAllBanners();
		}
		BannerFullTextSearchTerm term = form.toFullTextSearchTerm();
		term.setLanguage(LocaleContextHolder.getLocale().getLanguage());
		List<Long> ids = bannerRepository.findByFullTextSearchTerm(form.toFullTextSearchTerm());
		return new Paginator<Long>(ids, perPage);
	}
	
	private Paginator<Long> readAllBanners() {
		List<Long> results = bannerRepository.findId();
		if (results.isEmpty()) {
			return Paginator.getEmptyPaginator();
		}
		else {
			return new Paginator<Long>(results);
		}
	}
	
	public List<Banner> readBanners(Paginator<Long> paginator) {
		if (paginator == null || !paginator.hasElement()) return new ArrayList<Banner>();
		return readBanners(paginator.getElements());
	}
	
	public List<Banner> readBanners(Collection<Long> ids) {
		Set<Banner> results = new LinkedHashSet<Banner>(bannerRepository.findByIdIn(ids));
		List<Banner> banners = new ArrayList<>();
		for (long id : ids) {
			for (Banner banner : results) {
				if (id == banner.getId()) {
					banners.add(banner);
					break;
				}
			}
		}
		return banners;
	}
*/

	@CacheEvict(value="banners", allEntries=true)
	public Banner deleteBanner(long id, String language) {
		Banner banner = bannerRepository.findByIdForUpdate(id, language);
		bannerRepository.delete(banner);
		bannerRepository.decrementSortBySortGreaterThan(banner.getSort(), language);
		return banner;
	}

	@CacheEvict(value="banners", allEntries=true)
	public void updateBannerSort(List<Map<String, Object>> data, String language) {
		for (int i = 0; i < data.size(); i++) {
			Map<String, Object> map = data.get(i);
			if (map.get("item_id") != null) {
				Banner banner = bannerRepository.findByIdForUpdate(Long.parseLong((String) map.get("item_id")), language);
				if (banner != null) {
					banner.setSort(i);
					bannerRepository.save(banner);
				}
			}
		}
	}

//	@Cacheable(value="banners", key="'list.'+#type")
	public List<Banner> readBannersByType(Banner.Type type, String language) {
		return bannerRepository.findByType(type, language);
	}

//	@Cacheable(value="banners", key="'id.'+#id")
	public Banner readBannerById(long id, String language) {
		return bannerRepository.findById(id, language);
	}

//	@Cacheable(value="banners", key="'count.'+#type")
	public long countBannersByType(Banner.Type type, String language) {
		return bannerRepository.countByType(type, language);
	}
}
