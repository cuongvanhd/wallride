package org.wallride.web.controller.guest.author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wallride.core.domain.BlogLanguage;
import org.wallride.core.domain.User;
import org.wallride.core.service.UserService;
import org.wallride.core.support.Pagination;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthorIndexController {

	@Inject
	private UserService userService;

	@RequestMapping("/author")
	public String author(
			@PageableDefault(50) Pageable pageable,
			BlogLanguage blogLanguage,
			HttpServletRequest request,
			Model model) {
		AuthorSearchForm form = new AuthorSearchForm(){};
		List<User.Role> roles = new ArrayList<>();
		roles.add(User.Role.AUTHOR);
		form.setRoles(roles);
		Page<User> authors = userService.readUsers(form.toUserSearchRequest(), pageable);
		model.addAttribute("authors", authors);
		model.addAttribute("pageable", pageable);
		model.addAttribute("pagination", new Pagination<>(authors));
		return "author/index";
	}
}
