package org.wallride.web.controller.admin.article;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wallride.core.domain.Article;
import org.wallride.core.service.ArticleService;
import org.wallride.web.support.DomainObjectSelectModel;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ArticleSelectController {

	@Inject
	private ArticleService articleService;

	@RequestMapping(value="/{language}/articles/select-search")
	public @ResponseBody List<DomainObjectSelectModel> select(@RequestParam(required=false) String keyword) {
		ArticleSearchForm form = new ArticleSearchForm();
		form.setKeyword(keyword);
		Page<Article> paginator = articleService.readArticles(form.buildArticleSearchRequest());

		List<DomainObjectSelectModel> results = new ArrayList<>();
		if (paginator.hasContent()) {
			for (Article article : paginator) {
				DomainObjectSelectModel model = new DomainObjectSelectModel(article);
				results.add(model);
			}
		}
		return results;
	}

	@RequestMapping(value="/{language}/articles/select", method= RequestMethod.GET)
	public @ResponseBody DomainObjectSelectModel select(@RequestParam Long id, HttpServletResponse response) throws IOException {
		Article article = articleService.readArticleById(id, LocaleContextHolder.getLocale().getLanguage());
		if (article == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		DomainObjectSelectModel model = new DomainObjectSelectModel(article);
		return model;
	}
}
