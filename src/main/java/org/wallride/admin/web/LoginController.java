package org.wallride.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@RequestMapping
	public String form() {
		return "/login";
	}
	
	@RequestMapping(params="failed")
	public String failed(Model model) {
		model.addAttribute("failed", true);
		return "/login";
	}
}