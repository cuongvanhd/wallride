package org.wallride.web.controller.admin.banner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wallride.core.domain.Banner;
import org.wallride.core.service.BannerService;
import org.wallride.core.service.CategoryService;
import org.wallride.core.support.AuthorizedUser;

import javax.inject.Inject;

@Controller
@RequestMapping("/{language}/banners/edit")
public class BannerEditController {
	
	private static Logger logger = LoggerFactory.getLogger(BannerEditController.class);
	
	@Inject
	private BannerService bannerService;

	@Inject
	private CategoryService categoryService;

	@ModelAttribute("banner")
	public Banner banner(
			@PathVariable String language,
			@RequestParam long id) {
		return bannerService.readBannerById(id, language);
	}

//	@ModelAttribute("types")
//	public Banner.Type[] setupTypes() {
//		return Banner.Type.values();
//	}

	@RequestMapping(method= RequestMethod.GET)
	public String edit(
			@PathVariable String language,
			@RequestParam long id,
			Model model,
			RedirectAttributes redirectAttributes) {
		Banner banner = (Banner) model.asMap().get("banner");

		BannerEditForm form = BannerEditForm.fromDomainObject(banner);
		model.addAttribute("form", form);
		return "/banner/edit";
	}

	@RequestMapping(method= RequestMethod.POST, params="save")
	public String save(
			@PathVariable String language,
			@Validated @ModelAttribute("form") BannerEditForm form,
			BindingResult errors,
			AuthorizedUser authorizedUser,
			RedirectAttributes redirectAttributes) {
		if (errors.hasErrors()) {
			return "/banner/edit";
		}

		Banner banner = null;
		try {
			banner = bannerService.updateBanner(form.buildBannerUpdateRequest(), errors, authorizedUser);
		}
		catch (BindException e) {
			if (errors.hasErrors()) {
				logger.debug("Errors: {}", errors);
				return "/banner/edit";
			}
			throw new RuntimeException(e);
		}

		redirectAttributes.addFlashAttribute("savedBanner", banner);
		redirectAttributes.addAttribute("type", banner.getType());
		return "redirect:/_admin/{language}/banners/index?type={type}";
	}

	@RequestMapping(method= RequestMethod.POST, params="cancel")
	public String cancel(
			@PathVariable String language,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("language", language);
		return "redirect:/_admin/{language}/banners/index";
	}
}