package org.wallride.blog.web.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wallride.blog.service.BannerService;
import org.wallride.blog.service.PageService;
import org.wallride.blog.web.article.ArticleDescribeController;
import org.wallride.core.domain.Page;
import org.wallride.core.domain.PageTree;
import org.wallride.core.domain.Post;
import org.wallride.core.service.PageTreeService;
import org.wallride.core.web.HttpNotFoundException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping
public class PageDescribeController {

	@Inject
	private PageService pageService;

	@Inject
	private PageTreeService pageTreeService;

	@Inject
	private BannerService bannerService;

	@Inject
	private ArticleDescribeController articleDescribeController;

	@RequestMapping("/{language}/**")
	public String describe(
			@PathVariable String language,
			HttpServletRequest request,
			Model model,
			RedirectAttributes redirectAttributes) {
		String path = extractPathFromPattern(request);
		String[] codes = path.split("/");
		String lastCode = codes[codes.length - 1];

		Pattern pattern = Pattern.compile("(\\d{4})/(\\d{2})/(\\d{2})/(.+)");
		Matcher m = pattern.matcher(path);
		if (m.matches()) {
//			return articleDescribeController.describe("ja", 2013, 12, 24, "kijikiji", model, redirectAttributes);
			String v = articleDescribeController.describe(language, Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), m.group(4), model, redirectAttributes);
			System.out.println(v);
			return v;
		}

		Page page = pageService.readPage(lastCode);
		if (page == null) {
			throw new HttpNotFoundException();
		}

		PageTree pageTree = pageTreeService.readPageTree(language, Post.Status.PUBLISHED);
		Map<Page, String> parentPages = pageTree.getPaths(lastCode);

		model.addAttribute("page", page);
		model.addAttribute("breadcrumb", parentPages);
		model.addAttribute("code", lastCode);
		return "/page/describe";
	}

	private String extractPathFromPattern(final HttpServletRequest request){
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String ) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

		AntPathMatcher apm = new AntPathMatcher();
		String finalPath = apm.extractPathWithinPattern(bestMatchPattern, path);

		return finalPath;
	}
}
