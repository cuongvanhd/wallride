package org.wallride.blog.service;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.blog.web.PostSearchForm;
import org.wallride.core.domain.Post;
import org.wallride.core.repository.PostFullTextSearchTerm;
import org.wallride.core.repository.PostRepository;
import org.wallride.core.support.Paginator;

import javax.inject.Inject;
import java.util.*;

@Service
@Transactional(rollbackFor=Exception.class)
public class PostService {
	
	@Inject
	private PostRepository postRepository;

	public Paginator<Long> readPosts(PostSearchForm form, int perPage) {
		if (form.isEmpty()) {
			return readAllPosts();
		}
		PostFullTextSearchTerm term = form.toFullTextSearchTerm();
		term.setLanguage(LocaleContextHolder.getLocale().getLanguage());
		List<Long> ids = postRepository.findByFullTextSearchTerm(form.toFullTextSearchTerm());
		return new Paginator<Long>(ids, perPage);
	}
	
	private Paginator<Long> readAllPosts() {
		List<Long> results = postRepository.findId();
		if (results.isEmpty()) {
			return Paginator.getEmptyPaginator();
		}
		else {
			return new Paginator<Long>(results);
		}
	}
	
	public List<Post> readPosts(Paginator<Long> paginator) {
		if (paginator == null || !paginator.hasElement()) return new ArrayList<Post>();
		return readPosts(paginator.getElements());
	}
	
	public List<Post> readPosts(Collection<Long> ids) {
		Set<Post> results = new LinkedHashSet<Post>(postRepository.findByIdIn(ids));
		List<Post> posts = new ArrayList<>();
		for (long id : ids) {
			for (Post post : results) {
				if (id == post.getId()) {
					posts.add(post);
					break;
				}
			}
		}
		return posts;
	}
}
