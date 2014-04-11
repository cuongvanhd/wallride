package org.wallride.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.core.domain.PlayerNews;
import org.wallride.core.repository.PlayerNewsRepository;

import javax.inject.Inject;
import java.util.List;

@Service
@Transactional(rollbackFor=Exception.class)
public class PlayerNewsService {

	@Inject
	private PlayerNewsRepository playerNewsRepository;

	public List<PlayerNews> readNewses() {
		return playerNewsRepository.findAll();
	}

}
