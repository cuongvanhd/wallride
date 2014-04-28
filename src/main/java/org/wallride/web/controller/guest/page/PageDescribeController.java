package org.wallride.web.controller.guest.page;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.wallride.core.domain.Page;
import org.wallride.core.service.PageService;
import org.wallride.web.controller.guest.article.ArticleIndexController;
import org.wallride.web.support.HttpNotFoundException;

import javax.inject.Inject;

@Controller
@RequestMapping("/{language}/{code}")
public class PageDescribeController {

	@Inject
	private PageService pageService;

	@Inject
	private ArticleIndexController articleIndexController;

	@RequestMapping
	public String describe(
			@PathVariable String code,
			@PathVariable String language,
			@RequestParam(value="part", required=false) Integer part,
			Model model) {
		if (code.matches("[0-9]{4}")) {
			return articleIndexController.year(language, Integer.parseInt(code), new PageRequest(0, 10), model);
		}

		Page page = pageService.readPageByCode(code, language);
		if (page == null) {
			throw new HttpNotFoundException();
		}

		model.addAttribute("page", page);
		model.addAttribute("part", (part != null) ? part : 1);
		return "/page/describe";
	}
}
