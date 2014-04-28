package org.wallride.core.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.core.domain.Club;
import org.wallride.core.repository.ClubRepository;
import org.wallride.core.support.Paginator;

import javax.inject.Inject;
import java.util.*;

@Service
@Transactional(rollbackFor=Exception.class)
public class ClubService {

	@Inject
	private ClubRepository clubRepository;
	
//	public List<Long> searchClubs(ClubSearchRequest request) {
//		if (request.isEmpty()) {
//			return clubRepository.findId();
//		}
//		ClubFullTextSearchTerm term = request.toFullTextSearchTerm();
//		term.setLanguage(LocaleContextHolder.getLocale().getLanguage());
//		return clubRepository.findByFullTextSearchTerm(request.toFullTextSearchTerm());
//	}

	public List<Integer> searchClubs() {
		return clubRepository.findId();
	}

	public List<Club> readClubs(Paginator<Integer> paginator) {
		if (paginator == null || !paginator.hasElement()) return new ArrayList<Club>();
		return readClubs(paginator.getElements());
	}

	public List<Club> readClubs(Collection<Integer> ids) {
		Set<Club> results = new LinkedHashSet<Club>(clubRepository.findByIdIn(ids));
		List<Club> clubs = new ArrayList<>();
		for (long id : ids) {
			for (Club club : results) {
				if (id == club.getId()) {
					clubs.add(club);
					break;
				}
			}
		}
		return clubs;
	}

	public Club readClubById(int id) {
		return clubRepository.findById(id);
	}

	public List<Club> readClubsByLeagueId(int leagueId) {
		return clubRepository.findByLeagueId(leagueId);
	}
}
