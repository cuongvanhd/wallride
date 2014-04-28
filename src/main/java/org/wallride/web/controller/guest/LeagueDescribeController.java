package org.wallride.web.controller.guest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wallride.core.domain.Club;
import org.wallride.core.domain.League;
import org.wallride.core.service.ClubService;
import org.wallride.core.service.LeagueService;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("{language}/players/league/{leagueId}")
public class LeagueDescribeController {

	@Inject
	private LeagueService leagueService;

	@Inject
	private ClubService clubService;

	@RequestMapping
	public String describe(
			@PathVariable String language,
			@PathVariable Integer leagueId,
			HttpSession session,
			Model model) {

		League league = leagueService.readLeagueById(leagueId);
		List<Club> clubs = clubService.readClubsByLeagueId(leagueId);

		model.addAttribute("league", league);
		model.addAttribute("clubs", clubs);
		return "/players/league";
	}
}
