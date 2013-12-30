package org.wallride.blog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wallride.blog.service.BannerService;
import org.wallride.blog.service.ArticleService;
import org.wallride.core.domain.Banner;
import org.wallride.core.domain.Setting;
import org.wallride.core.service.SettingService;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController {

	@Inject
	private SettingService settingService;

	@Inject
	private BannerService bannerService;

	@RequestMapping
	public String index(RedirectAttributes redirectAttributes) {
		String defaultLanguage = settingService.readSettingAsString(Setting.Key.DEFAULT_LANGUAGE);
		redirectAttributes.addAttribute("language", defaultLanguage);
		return "redirect:/{language}/";
	}

	@RequestMapping("/{language}/")
	public String index(Model model) {

		List<Banner> mainBanners = bannerService.readBanners(Banner.Type.MAIN);
		List<Banner> subBanners = bannerService.readBanners(Banner.Type.SUB);
		List<Banner> asideBanners = bannerService.readBanners(Banner.Type.ASIDE);
		model.addAttribute("mainBanners", mainBanners);
		model.addAttribute("subBanners", subBanners);
		model.addAttribute("asideBanners", asideBanners);

		return "/index";
	}
}
