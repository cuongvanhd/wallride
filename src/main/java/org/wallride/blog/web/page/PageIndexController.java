package org.wallride.blog.web.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wallride.blog.service.BannerService;
import org.wallride.blog.service.PageService;
import org.wallride.blog.web.article.ArticleIndexController;
import org.wallride.core.domain.Banner;
import org.wallride.core.domain.PageTree;
import org.wallride.core.service.DefaultModelAttributeService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PageIndexController {

//	/page/[:yyyy]/[:mm]/[:dd]/[:code]
//	/categories/[:code]/[:code]/[:code]/[:code]/
//	/tag/[:code]/

	@Inject
	private ArticleIndexController articleIndexController;

	@Inject
	private PageService pageService;

	@Inject
	private DefaultModelAttributeService defaultModelAttributeService;

	@Inject
	private BannerService bannerService;

	@RequestMapping("/{language}/{code}/")
	public String index(
			@PathVariable String code,
			@PathVariable String language,
			HttpServletRequest request,
			HttpSession session,
			Model model) {
		if (code.matches("[0-9]{4}")) {
			return articleIndexController.year(language, Integer.parseInt(code), null, null, session, model);
		}
		if (code.equals("category")) {
			return articleIndexController.category(language, null, null, request, session, model);
		}

		PageTree pageTree = defaultModelAttributeService.readPageTree(language);
		PageTree.Node node = pageTree.getNodeByCode(code);
		PageTree.Node child = node.getChildren().get(0);

		model.addAttribute("page", child.getPage());
		model.addAttribute("category", code);

		List<Banner> asideBanners = bannerService.readBanners(Banner.Type.ASIDE, language);
		model.addAttribute("asideBanners", asideBanners);

		return "/page/describe";
	}


//	@RequestMapping("/{language}/categories/**")
//	public void category(HttpServletRequest request) {
//		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
//		String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
//		String temp = new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
//		System.out.println(temp);
//	}
}
