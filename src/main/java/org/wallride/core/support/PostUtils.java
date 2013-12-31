package org.wallride.core.support;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.wallride.core.domain.Article;
import org.wallride.core.domain.Page;
import org.wallride.core.domain.PageTree;
import org.wallride.core.service.DefaultModelAttributeService;

import javax.inject.Inject;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class PostUtils {

	@Inject
	private DefaultModelAttributeService defaultModelAttributeService;

	public String path(Article article) {
		return String.format("%d/%d/%d/%s",
				article.getDate().getYear(),
				article.getDate().getMonthOfYear(),
				article.getDate().getDayOfMonth(),
				article.getCode());
	}

	public String path(Page page) {
		PageTree pageTree = defaultModelAttributeService.readPageTree(LocaleContextHolder.getLocale().getLanguage());
		List<String> parentCodes = new LinkedList<>();
		if (page.getParent() != null) {
			Page parent = page.getParent();
			while (parent != null) {
				parentCodes.add(parent.getCode());
				parent = parent.getParent();
			}
		}
		Collections.reverse(parentCodes);
		parentCodes.add(page.getCode());
		return StringUtils.collectionToDelimitedString(parentCodes, "/");
	}
}
