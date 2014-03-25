package org.wallride.core.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.core.domain.League;
import org.wallride.core.repository.LeagueRepository;
import org.wallride.core.support.Paginator;

import javax.inject.Inject;
import java.util.*;

@Service
@Transactional(rollbackFor=Exception.class)
public class LeagueService {

	@Inject
	private LeagueRepository leagueRepository;
	
//	public List<Long> searchLeagues(LeagueSearchRequest request) {
//		if (request.isEmpty()) {
//			return leagueRepository.findId();
//		}
//		LeagueFullTextSearchTerm term = request.toFullTextSearchTerm();
//		term.setLanguage(LocaleContextHolder.getLocale().getLanguage());
//		return leagueRepository.findByFullTextSearchTerm(request.toFullTextSearchTerm());
//	}

	public List<Integer> searchLeagues() {
		return leagueRepository.findId();
	}


	public List<League> readLeagues(Paginator<Integer> paginator) {
		if (paginator == null || !paginator.hasElement()) return new ArrayList<League>();
		return readLeagues(paginator.getElements());
	}

	public List<League> readLeagues(Collection<Integer> ids) {
		Set<League> results = new LinkedHashSet<League>(leagueRepository.findByIdIn(ids));
		List<League> leagues = new ArrayList<>();
		for (long id : ids) {
			for (League league : results) {
				if (id == league.getId()) {
					leagues.add(league);
					break;
				}
			}
		}
		return leagues;
	}

	public League readLeagueById(int id) {
		return leagueRepository.findById(id);
	}
}
