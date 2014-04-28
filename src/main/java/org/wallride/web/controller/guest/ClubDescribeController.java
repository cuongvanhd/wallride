package org.wallride.web.controller.guest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wallride.core.service.ClubService;
import org.wallride.core.service.LeagueService;
import org.wallride.core.service.PlayerService;

import javax.inject.Inject;

@Controller
@RequestMapping("{language}/players/club/{leagueId}/{clubId}")
public class ClubDescribeController {

	@Inject
	private LeagueService leagueService;

	@Inject
	private ClubService clubService;

	@Inject
	private PlayerService playerService;

//	@RequestMapping
//	public String describe(
//			@PathVariable Integer leagueId,
//			@PathVariable Integer clubId,
//			HttpSession session,
//			Model model) {
//		League league =leagueService.readLeagueById(leagueId);
//		Club club =clubService.readClubById(clubId);
////		List<Player> players = playerService.readPlayersByLeagueIdAndClubId(leagueId, clubId);
//		model.addAttribute("league", league);
//		model.addAttribute("club", club);
////		model.addAttribute("players", players);
//		return "/players/club";
//	}
}
