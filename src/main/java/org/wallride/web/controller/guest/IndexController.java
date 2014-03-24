package org.wallride.web.controller.guest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wallride.core.domain.Setting;
import org.wallride.core.service.SettingService;

import javax.inject.Inject;

@Controller
@RequestMapping("/")
public class IndexController {

	@Inject
	private SettingService settingService;

	@RequestMapping
	public String index(RedirectAttributes redirectAttributes) {
		String defaultLanguage = settingService.readSettingAsString(Setting.Key.DEFAULT_LANGUAGE);
		redirectAttributes.addAttribute("language", defaultLanguage);
		return "redirect:/{language}/";
	}

	@RequestMapping("/{language}/")
	public String index(
			@PathVariable String language,
			Model model) {
		return "/index";
	}
}
