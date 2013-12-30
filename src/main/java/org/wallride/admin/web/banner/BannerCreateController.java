package org.wallride.admin.web.banner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wallride.admin.service.BannerService;
import org.wallride.admin.support.AuthorizedUser;
import org.wallride.core.domain.Banner;

import javax.inject.Inject;

@Controller
@RequestMapping("/{language}/banners/create")
public class BannerCreateController {

	private static Logger logger = LoggerFactory.getLogger(BannerCreateController.class);

	@Inject
	private BannerService bannerService;

	@ModelAttribute("form")
	public BannerCreateForm bannerCreateForm() {
		return new BannerCreateForm();
	}

	@ModelAttribute("types")
	public Banner.Type[] setupTypes() {
		return Banner.Type.values();
	}

	@RequestMapping(method= RequestMethod.GET)
	public String create(@PathVariable String language) {
		return "/banner/create";
	}

	@RequestMapping(method= RequestMethod.POST)
	public String publish(
			@PathVariable String language,
			@Validated @ModelAttribute("form") BannerCreateForm form,
			BindingResult errors,
			AuthorizedUser authorizedUser,
			RedirectAttributes redirectAttributes) {
		if (errors.hasErrors()) {
			return "/banner/create";
		}

		Banner banner = null;
		try {
			banner = bannerService.createBanner(form, errors, authorizedUser);
		}
		catch (BindException e) {
			if (errors.hasErrors()) {
				logger.debug("Errors: {}", errors);
				return "/banner/create";
			}
			throw new RuntimeException(e);
		}

		redirectAttributes.addFlashAttribute("savedBanner", banner);
		redirectAttributes.addAttribute("type", banner.getType());
		return "redirect:/_admin/{language}/banners/index?type={type}";
	}
}