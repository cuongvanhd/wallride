package org.wallride.web.controller.guest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wallride.core.domain.Club;
import org.wallride.core.domain.League;
import org.wallride.core.domain.Movement;
import org.wallride.core.domain.Player;
import org.wallride.core.service.ClubService;
import org.wallride.core.service.LeagueService;
import org.wallride.core.service.MovementService;
import org.wallride.core.service.PlayerService;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("{language}/player/{leagueId}/{clubId}/{playerId}")
public class PlayerDescribeController {

	@Inject
	private LeagueService leagueService;

	@Inject
	private ClubService clubService;

	@Inject
	PlayerService playerService;

	@Inject
	MovementService movementService;

	@RequestMapping
	public String describe(
			@PathVariable String language,
			@PathVariable Integer leagueId,
			@PathVariable Integer clubId,
			@PathVariable Integer playerId,
			HttpSession session,
			Model model) {
		League league =leagueService.readLeagueById(leagueId);
		Club club =clubService.readClubById(clubId);
		Player player = playerService.readPlayerById(playerId);
		List<Movement> histories = movementService.readMovementsByPlayerId(playerId);

		model.addAttribute("league", league);
		model.addAttribute("club", club);
		model.addAttribute("player", player);
		model.addAttribute("histories", histories);
		return "/player";
	}

}
