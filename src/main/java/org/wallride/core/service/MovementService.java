package org.wallride.core.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor=Exception.class)
public class MovementService {

/*	@Inject
	private MovementRepository movementRepository;
	
//	public List<Long> searchMovements(MovementSearchRequest request) {
//		if (request.isEmpty()) {
//			return movementRepository.findId();
//		}
//		MovementFullTextSearchTerm term = request.toFullTextSearchTerm();
//		term.setLanguage(LocaleContextHolder.getLocale().getLanguage());
//		return movementRepository.findByFullTextSearchTerm(request.toFullTextSearchTerm());
//	}

	public List<Integer> searchMovements() {
		return movementRepository.findId();
	}

	public List<Movement> readMovements(Paginator<Integer> paginator) {
		if (paginator == null || !paginator.hasElement()) return new ArrayList<Movement>();
		return readMovements(paginator.getElements());
	}

	public List<Movement> readMovements(Collection<Integer> ids) {
		Set<Movement> results = new LinkedHashSet<Movement>(movementRepository.findByIdIn(ids));
		List<Movement> movements = new ArrayList<>();
		for (long id : ids) {
			for (Movement movement : results) {
				if (id == movement.getId()) {
					movements.add(movement);
					break;
				}
			}
		}
		return movements;
	}

	public Movement readMovementById(int id) {
		return movementRepository.findById(id);
	}

	public List<Movement> readMovementsByPlayerId(int playerId) {
		return movementRepository.findByPlayerId(playerId);
	}*/
}
