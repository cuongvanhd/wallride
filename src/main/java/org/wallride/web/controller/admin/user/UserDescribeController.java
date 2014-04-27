package org.wallride.web.controller.admin.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.wallride.core.domain.Post;
import org.wallride.core.domain.User;
import org.wallride.core.service.ArticleService;
import org.wallride.core.service.UserService;
import org.wallride.web.support.DomainObjectDescribeController;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping(value="/{language}/users/describe", method= RequestMethod.GET)
public class UserDescribeController extends DomainObjectDescribeController<User, UserSearchForm> {
	
	@Inject
	private UserService userService;

	@Inject
	private ArticleService articleService;

	@ModelAttribute("articleCounts")
	public Map<Long, Long> articleCounts(@PathVariable String language) {
		return articleService.countArticlesByAuthorIdGrouped(Post.Status.PUBLISHED, language);
	}

	@RequestMapping
	public String describe(
			@RequestParam long id,
			@RequestParam(required=false) String token,
			Model model,
			HttpSession session) {
		return super.requestMappingDescribe(id, token, model, session);
	}

	@Override
	protected Class<UserSearchForm> getDomainObjectSearchFormClass() {
		return UserSearchForm.class;
	}

	@Override
	protected String getModelAttributeName() {
		return "user";
	}

	@Override
	protected String getViewName() {
		return "/user/describe";
	}

	@Override
	protected User readDomainObject(long id) {
		return userService.readUserById(id);
	}
}