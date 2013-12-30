package org.wallride.blog.web;

import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.wallride.core.domain.Post;
import org.wallride.core.repository.ArticleFullTextSearchTerm;
import org.wallride.core.repository.PostFullTextSearchTerm;
import org.wallride.core.web.DomainObjectSearchForm;

@SuppressWarnings("serial")
public class PostSearchForm extends DomainObjectSearchForm {
	
	private String keyword;

	private String language;

	public PostSearchForm() {
		this.language = LocaleContextHolder.getLocale().getLanguage();
	}

	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public boolean isEmpty() {
		if (StringUtils.hasText(getKeyword())) {
			return false;
		}
		if (StringUtils.hasText(getLanguage())) {
			return false;
		}
		return true;
	}
	
	public PostFullTextSearchTerm toFullTextSearchTerm() {
		PostFullTextSearchTerm term = new PostFullTextSearchTerm();
		BeanUtils.copyProperties(this, term);
		return term;
	}
}
