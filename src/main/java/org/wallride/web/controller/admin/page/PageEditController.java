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
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/{language}/pages/edit")
public class PageEditController {
	
	private static Logger logger = LoggerFactory.getLogger(PageEditController.class); 
	
	@Inject
	private PageService pageService;

	@ModelAttribute("page")
	public Page page(
			@PathVariable String language,
			@RequestParam long id) {
		return pageService.readPageById(id, language);
	}
	
	@ModelAttribute("pageTree")
	public PageTree pageTree(@PathVariable String language) {
		return pageService.readPageTree(language);
	}

	@RequestMapping(method=RequestMethod.GET)
	public String edit(
			@PathVariable String language,
			@RequestParam long id,
			Model model,
			RedirectAttributes redirectAttributes) {
		Page page = pageService.readPageById(id, language);
		if (!language.equals(page.getLanguage())) {
			redirectAttributes.addAttribute("language", language);
			return "redirect:/_admin/{language}/pages/";
		}

		PageEditForm form = PageEditForm.fromDomainObject(page);
		model.addAttribute("form", form);
		return "/page/edit";
	}
	
	@RequestMapping(method=RequestMethod.POST, params="unpublish")
	public String unpublish(
			@PathVariable String language,
			@Validated @ModelAttribute("form") PageEditForm form,
			BindingResult errors,
			AuthorizedUser authorizedUser,
			RedirectAttributes redirectAttributes) {
		form.getBodies().removeAll(Collections.singleton(null));
		if (CollectionUtils.isEmpty(form.getBodies())) {
			List<String> bodies = new ArrayList<>();
			bodies.add("");
			form.setBodies(bodies);
		}
		if (errors.hasErrors()) {
			for (ObjectError error : errors.getAllErrors()) {
				if (!"validation.NotNull".equals(error.getCode())) {
					return "/page/edit";
				}
			}
		}

		Page page = null;
		try {
			page = pageService.updatePage(form.buildPageUpdateRequest(), errors, Post.Status.UNPUBLISHED, authorizedUser);
		}
		catch (BindException e) {
			if (errors.hasErrors()) {
				logger.debug("Errors: {}", errors);
				return "/page/edit";
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
			@Validated({Default.class, PageEditForm.GroupPublish.class}) @ModelAttribute("form") PageEditForm form,
			BindingResult errors,
			AuthorizedUser authorizedUser,
			RedirectAttributes redirectAttributes) {
		form.getBodies().removeAll(Collections.singleton(null));
		if (CollectionUtils.isEmpty(form.getBodies())) {
			List<String> bodies = new ArrayList<>();
			bodies.add("");
			form.setBodies(bodies);
		}
		if (errors.hasErrors()) {
			return "/page/edit";
		}

		Page page = null;
		try {
			page = pageService.updatePage(form.buildPageUpdateRequest(), errors, Post.Status.PUBLISHED, authorizedUser);
		}
		catch (BindException e) {
			if (errors.hasErrors()) {
				logger.debug("Errors: {}", errors);
				return "/page/edit";
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
			@Valid @ModelAttribute("form") PageEditForm form,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("id", form.getId());
		return "redirect:/_admin/pages/describe/{id}";
	}
}