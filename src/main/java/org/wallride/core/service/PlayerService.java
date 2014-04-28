package org.wallride.core.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor=Exception.class)
public class PlayerService {

/*	@Inject
	private PlayerRepository playerRepository;

//	public List<Long> searchPlayers(PlayerSearchRequest request) {
//		if (request.isEmpty()) {
//			return playerRepository.findId();
//		}
//		PlayerFullTextSearchTerm term = request.toFullTextSearchTerm();
//		term.setLanguage(LocaleContextHolder.getLocale().getLanguage());
//		return playerRepository.findByFullTextSearchTerm(request.toFullTextSearchTerm());
//	}

	public List<Integer> searchPlayers() {
		return playerRepository.findId();
	}

	public List<Player> readPlayers(Page<Player> paginator) {
		if (paginator == null || !paginator.hasElement()) return new ArrayList<Player>();
		return readPlayers(paginator.getElements());
	}

	public List<Player> readPlayers(Collection<Integer> ids) {
		Set<Player> results = new LinkedHashSet<Player>(playerRepository.findByIdIn(ids));
		List<Player> players = new ArrayList<>();
		for (long id : ids) {
			for (Player player : results) {
				if (id == player.getId()) {
					players.add(player);
					break;
				}
			}
		}
		return players;
	}

	public Player readPlayerById(int id) {
		return playerRepository.findById(id);
	}

//	public List<Player> readPlayersByLeagueIdAndClubId(int leagueId, int clubId) {
//		return playerRepository.findByLeagueIdAndClubId(leagueId, clubId);
//	}*/
}
