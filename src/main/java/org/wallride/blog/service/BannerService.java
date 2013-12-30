package org.wallride.blog.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.core.domain.Banner;
import org.wallride.core.repository.BannerRepository;

import javax.inject.Inject;
import java.util.List;

@Service
@Transactional(rollbackFor=Exception.class)
public class BannerService {

	@Inject
	private BannerRepository bannerRepository;

//	@Cacheable(value="banners", key="'list.'+#type")
	public List<Banner> readBanners(Banner.Type type, String language) {
		return bannerRepository.findByType(type, language);
	}
}
