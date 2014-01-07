package org.wallride.core.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.core.domain.Page;
import org.wallride.core.domain.PageTree;
import org.wallride.core.domain.Post;
import org.wallride.core.repository.PageRepository;

import javax.inject.Inject;
import java.util.List;

@Service
@Transactional(rollbackFor=Exception.class)
public class PageTreeService {
	
	@Inject
	private PageRepository pageRepository;

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
