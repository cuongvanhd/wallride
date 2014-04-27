package org.wallride.web.controller.guest;

import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;
import org.wallride.core.domain.Article;
import org.wallride.core.service.ArticleService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/{language}/index.php/**")
public class LegacyArticleController {

	@Inject
	ArticleService articleService;

	@RequestMapping
	public String show(
			HttpServletRequest request,
			@PathVariable("language") String language,
			HttpServletResponse response) {
		String path = extractPathFromPattern(request);
		String[] codes = path.split("/");
		String lastCode = codes[codes.length - 1];

		Article a = articleService.readArticleByCode(lastCode, "ja");
		return String.format("redirect:/%s/%s", a.getCreatedAt().toString("yyyy/MM/dd"), lastCode);
	}

	private String extractPathFromPattern(final HttpServletRequest request){
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String ) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

		AntPathMatcher apm = new AntPathMatcher();
		String finalPath = apm.extractPathWithinPattern(bestMatchPattern, path);

		return finalPath;
	}
}
