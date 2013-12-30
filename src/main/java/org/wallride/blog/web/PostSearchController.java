package org.wallride.blog.web;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wallride.blog.service.PostService;
import org.wallride.core.domain.Post;
import org.wallride.core.support.Paginator;
import org.wallride.core.web.DomainObjectSearchCondition;
import org.wallride.core.web.DomainObjectSearchController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("/{language}/search")
public class PostSearchController extends DomainObjectSearchController<Post, PostSearchForm> {

	@Inject
	private PostService postService;

	@RequestMapping(method=RequestMethod.GET)
	public String index(
			@RequestParam(required=false) String token,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response,
			HttpSession session)
			throws Exception {
		return super.requestMappingIndex(token, model, request, response, session);
	}

	@RequestMapping(params="page")
	public String page(
			@RequestParam int page,
			@RequestParam(required=false) String token,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response,
			HttpSession session) {
		return super.requestMappingPage(page, token, model, request, response, session);
	}

	@RequestMapping(params="search")
	public String search(
			@Valid PostSearchForm form,
			BindingResult result,
			Model model,
			HttpSession session,
			RedirectAttributes redirectAttributes) {
		return super.requestMappingSearch(form, result, model, session, redirectAttributes);
	}

	@Override
	protected Class<PostSearchForm> getDomainObjectSearchFormClass() {
		return PostSearchForm.class;
	}

	@Override
	protected String getModelAttributeName() {
		return "posts";
	}

	@Override
	protected String getViewName() {
		return "/search";
	}

	@Override
	protected String getRedirectViewName() {
		return "redirect:/{language}/search";
	}

	@Override
	protected Paginator<Long> readDomainObjects(PostSearchForm form, int perPage) {
		return postService.readPosts(form, perPage);
	}

	@Override
	protected Collection<Post> readDomainObjects(Paginator<Long> paginator) {
		return postService.readPosts(paginator);
	}

	@Override
	protected boolean validateCondition(DomainObjectSearchCondition<PostSearchForm> condition, HttpServletRequest request, HttpServletResponse response) {
		String language = LocaleContextHolder.getLocale().getLanguage();
		return language.equals(condition.getForm().getLanguage());
	}
}