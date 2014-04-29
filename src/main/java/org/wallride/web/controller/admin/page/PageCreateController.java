package org.wallride.web.controller.admin.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wallride.core.domain.Page;
import org.wallride.core.domain.PageTree;
import org.wallride.core.domain.Post;
import org.wallride.core.service.PageService;
import org.wallride.core.support.AuthorizedUser;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/{language}/pages/create")
public class PageCreateController {

	private static Logger logger = LoggerFactory.getLogger(PageCreateController.class);

	@Inject
	private PageService pageService;

	@ModelAttribute("form")
	public PageCreateForm pageCreateForm() {
		PageCreateForm form = new PageCreateForm();
		List<String> bodies = new ArrayList<>();
		bodies.add("");
		form.setBodies(bodies);
		return form;
	}

	@ModelAttribute("pageTree")
	public PageTree pageTree(@PathVariable String language) {
		return pageService.readPageTree(language);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String create() {
		return "/page/create";
	}

	@RequestMapping(method=RequestMethod.GET, params="part=page-fieldset")
	public String partPageFieldset(@PathVariable String language, Model model) {
		PageTree pageTree = pageService.readPageTree(language);
		model.addAttribute("pageTree", pageTree);
		return "/page/create::#page-fieldset";
	}

	@RequestMapping(method=RequestMethod.POST, params="unpublish")
	public String unpublish(
			@PathVariable String language,
			@Validated @ModelAttribute("form") PageCreateForm form,
			BindingResult errors,
			AuthorizedUser authorizedUser,
			RedirectAttributes redirectAttributes) {
		form.getBodies().removeAll(Collections.singleton(null));
		if (errors.hasErrors()) {
			for (ObjectError error : errors.getAllErrors()) {
				if (!"validation.NotNull".equals(error.getCode())) {
					return "/page/create";
				}
			}
		}

		Page page = null;
		try {
			form.setStatus(Post.Status.UNPUBLISHED);
			page = pageService.createPage(form.buildPageCreateRequest(), errors, authorizedUser);
		}
		catch (BindException e) {
			if (errors.hasErrors()) {
				logger.debug("Errors: {}", errors);
				return "/page/create";
			}
			throw new RuntimeException(e);
		}

		redirectAttributes.addFlashAttribute("savedPage", page);
		redirectAttributes.addAttribute("language", language);
		redirectAttributes.addAttribute("id", page.getId());
		return "redirect:/_admin/{language}/pages/describe?id={id}";
	}

	@RequestMapping(method=RequestMethod.POST, params="publish")
	public String publish(
			@PathVariable String language,
			@Validated({Default.class, PageCreateForm.GroupPublish.class}) @ModelAttribute("form") PageCreateForm form,
			BindingResult errors,
			AuthorizedUser authorizedUser,
			RedirectAttributes redirectAttributes) {
		form.getBodies().removeAll(Collections.singleton(null));
		if (CollectionUtils.isEmpty(form.getBodies())) {
			errors.rejectValue("NotNull", "bodies");
			List<String> bodies = new ArrayList<>();
			bodies.add("");
			form.setBodies(bodies);
		}
		if (errors.hasErrors()) {
			return "/page/create";
		}

		Page page = null;
		try {
			form.setStatus(Post.Status.PUBLISHED);
			page = pageService.createPage(form.buildPageCreateRequest(), errors, authorizedUser);
		}
		catch (BindException e) {
			if (errors.hasErrors()) {
				logger.debug("Errors: {}", errors);
				return "/page/create";
			}
			throw new RuntimeException(e);
		}

		redirectAttributes.addFlashAttribute("savedPage", page);
		redirectAttributes.addAttribute("language", language);
		redirectAttributes.addAttribute("id", page.getId());
		return "redirect:/_admin/{language}/pages/describe?id={id}";
	}

	@RequestMapping(method=RequestMethod.POST, params="cancel")
	public String cancel(
			@PathVariable String language,
			@Valid @ModelAttribute("form") PageCreateForm form,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("language", language);
		return "redirect:/_admin/{language}/page/";
	}
}