package org.wallride.web.controller.guest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("{language}/club/{leagueId}/{clubId}")
public class ClubDescribeController {

	@RequestMapping
	public String describe(
			@PathVariable String language,
			@PathVariable Long leagueId,
			@PathVariable Long clubId,
			HttpSession session,
			Model model) {

		//model.addAttribute("leagues", leagues);
		return "/club";
	}
}
