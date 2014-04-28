package org.wallride.web.controller.guest;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wallride.core.domain.Banner;
import org.wallride.core.service.BannerService;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping("/{language}/")
public class IndexController {

	@Inject
	private BannerService bannerService;

	@RequestMapping
	public String index(
			@PathVariable String language,
			Model model) {
		List<Banner> banners = bannerService.readBannersByType(Banner.Type.MAIN, language);
		model.addAttribute("banners", banners);
		model.addAttribute("home", true);
		return "/index";
	}

}
