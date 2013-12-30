package org.wallride.admin.web.banner;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.wallride.admin.service.BannerService;
import org.wallride.core.domain.Banner;

import javax.inject.Inject;
import java.util.List;

//TODO 中身
@Controller
@RequestMapping("/{language}/banners/index")
public class BannerIndexController {

	@Inject
	private BannerService bannerService;

	@ModelAttribute("form")
	public BannerCreateForm bannerCreateForm() {
		return new BannerCreateForm();
	}

	@ModelAttribute("countMain")
	public long countMain() {
		return bannerService.countBannersByType(Banner.Type.MAIN);
	}

	@ModelAttribute("countSub")
	public long countSub() {
		return bannerService.countBannersByType(Banner.Type.SUB);
	}

	@ModelAttribute("countAside")
	public long countAside() {
		return bannerService.countBannersByType(Banner.Type.ASIDE);
	}

	@RequestMapping
	public String index(@PathVariable String language, Model model, @RequestParam(required = false) Banner.Type type) {
		if(type == null) {
			type = Banner.Type.MAIN;
		}
		List<Banner> banners = bannerService.readBannersByType(type);
		model.addAttribute("type", type);
		model.addAttribute("banners", banners);
		return "/banner/index";
	}

/*
	@RequestMapping(params="part=banner-create-dialog")
	public String partBannerCreateDialog(@PathVariable String language, @RequestParam(required=false) Long parentId, Model model) {
		return "/banner/index::#banner-create-dialog";
	}

	@RequestMapping(params="part=banner-edit-dialog")
	public String partBannerEditDialog(@PathVariable String language, @RequestParam long id, Model model) {
		return "/banner/index::#banner-edit-dialog";
	}
*/

	@RequestMapping(params="part=banner-delete-dialog")
	public String partBannerDeleteDialog(@RequestParam long id, Model model) {
		model.addAttribute("targetId", id);
		return "/banner/index::#banner-delete-dialog";
	}
}