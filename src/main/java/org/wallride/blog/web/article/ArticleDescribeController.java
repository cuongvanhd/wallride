package org.wallride.blog.web.article;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wallride.blog.service.ArticleService;
import org.wallride.blog.service.BannerService;
import org.wallride.core.domain.Article;
import org.wallride.core.domain.Banner;
import org.wallride.core.web.HttpNotFoundException;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping("/{language}/{year}/{month}/{day}/{code}")
public class ArticleDescribeController {

	@Inject
	private ArticleService articleService;

	@Inject
	private BannerService bannerService;

	@RequestMapping
	public String describe(
			@PathVariable String language,
			@PathVariable int year,
			@PathVariable int month,
			@PathVariable int day,
			@PathVariable String code,
			Model model,
			RedirectAttributes redirectAttributes) {
		Article article = articleService.readArticle(code, language);
		if (article == null) {
			throw new HttpNotFoundException();
		}

		LocalDate date = new LocalDate(year, month, day);
		if (!article.getDate().toLocalDate().equals(date)) {
			redirectAttributes.addAttribute("language", language);
			redirectAttributes.addAttribute("year", article.getDate().getYear());
			redirectAttributes.addAttribute("month", article.getDate().getMonthOfYear());
			redirectAttributes.addAttribute("day", article.getDate().getDayOfMonth());
			redirectAttributes.addAttribute("code", code);
			return "redirect:/{language}/{year}/{month}/{day}/{code}";
		}

		List<Banner> asideBanners = bannerService.readBanners(Banner.Type.ASIDE, language);

		model.addAttribute("article", article);
		model.addAttribute("asideBanners", asideBanners);
		return "/article/describe";
	}
}
