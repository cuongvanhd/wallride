package org.wallride.blog.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.core.domain.Page;
import org.wallride.core.domain.Post;
import org.wallride.core.repository.PageRepository;

import javax.inject.Inject;

@Service
@Transactional(rollbackFor=Exception.class)
public class PageService {

	@Inject
	private PageRepository pageRepository;

	@Cacheable(value="pages", key="'code.'+#code+'.'+#language+'.PUBLISHED'")
	public Page readPage(String code, String language) {
		return pageRepository.findByCode(code, language, Post.Status.PUBLISHED);
	}
}
