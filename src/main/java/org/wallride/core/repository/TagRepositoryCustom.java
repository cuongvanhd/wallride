package org.wallride.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wallride.core.domain.Tag;

public interface TagRepositoryCustom {

	Page<Tag> findByFullTextSearchTerm(TagFullTextSearchTerm term, Pageable pageable);
}
