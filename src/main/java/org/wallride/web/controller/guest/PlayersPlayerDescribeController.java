package org.wallride.web.controller.guest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wallride.core.domain.*;
import org.wallride.core.service.*;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping("{language}/players/player/{leagueId}/{clubId}/{playerId}")
public class PlayersPlayerDescribeController {

	@Inject
	private LeagueService leagueService;

	@Inject
	private ClubService clubService;

	@Inject
	private PlayerService playerService;

	@Inject
	private PlayerNewsService playerNewsService;

	@Inject
	private PlayerItemService playerItemService;

	@Inject
	MovementService movementService;

	@RequestMapping
	public String describe(
			@PathVariable String language,
			@PathVariable Integer leagueId,
			@PathVariable Integer clubId,
			@PathVariable Integer playerId,
			Model model) {
		League league =leagueService.readLeagueById(leagueId);
		//TODO boolean playoffParticipate = leagueService.isPlayoffParticipate(leagueId);
		Club club =clubService.readClubById(clubId);
		Player player = playerService.readPlayerById(playerId);
		List<Movement> histories = movementService.readMovementsByPlayerId(playerId);
		List<PlayerNews> newses = playerNewsService.readNewses();
		//TODO List<AmazonItemResult> items = playerItemService.readItems();

		//model.addAttribute("items", items);
		model.addAttribute("newses", newses);
		model.addAttribute("league", league);
		model.addAttribute("club", club);
		model.addAttribute("player", player);
		model.addAttribute("histories", histories);
		return "/players/player";
	}

}
