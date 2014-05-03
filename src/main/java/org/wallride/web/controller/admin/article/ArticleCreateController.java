package org.wallride.web.controller.admin.article;

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
import org.wallride.core.domain.Article;
import org.wallride.core.domain.CategoryTree;
import org.wallride.core.domain.Post;
import org.wallride.core.service.ArticleService;
import org.wallride.core.service.CategoryService;
import org.wallride.core.support.AuthorizedUser;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.groups.Default;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/{language}/articles/create")
public class ArticleCreateController {

	private static Logger logger = LoggerFactory.getLogger(ArticleCreateController.class);

	@Inject
	private ArticleService articleService;

	@Inject
	private CategoryService categoryService;

	@ModelAttribute("form")
	public ArticleCreateForm articleCreateForm() {
		ArticleCreateForm form = new ArticleCreateForm();
		List<String> bodies = new ArrayList<>();
		bodies.add("");
		form.setBodies(bodies);
		return form;
	}

	@ModelAttribute("categoryTree")
	public CategoryTree categoryTree(@PathVariable String language) {
		return categoryService.readCategoryTree(language);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String create() {
		return "/article/create";
	}

	@RequestMapping(method=RequestMethod.GET, params="part=category-fieldset")
	public String partCategoryFieldset(@PathVariable String language, Model model) {
		CategoryTree categoryTree = categoryService.readCategoryTree(language);
		model.addAttribute("categoryTree", categoryTree);
		return "/article/create::#category-fieldset";
	}

	@RequestMapping(method=RequestMethod.POST, params="unpublish")
	public String unpublish(
			@PathVariable String language,
			@Validated @ModelAttribute("form") ArticleCreateForm form,
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
					return "/article/create";
				}
			}
		}

		Article article = null;
		try {
			article = articleService.createArticle(form.buildArticleCreateRequest(language), errors, Post.Status.UNPUBLISHED, authorizedUser);
		}
		catch (BindException e) {
			if (errors.hasErrors()) {
				logger.debug("Errors: {}", errors);
				return "/article/create";
			}
			throw new RuntimeException(e);
		}
		catch (IOException e) {
			if (errors.hasErrors()) {
				logger.debug("Errors: {}", errors);
				return "/article/edit";
			}
			throw new RuntimeException(e);
		}

		redirectAttributes.addFlashAttribute("savedArticle", article);
		redirectAttributes.addAttribute("language", language);
		redirectAttributes.addAttribute("id", article.getId());
		return "redirect:/_admin/{language}/articles/describe?id={id}";
	}

	@RequestMapping(method=RequestMethod.POST, params="publish")
	public String publish(
			@PathVariable String language,
			@Validated({Default.class, ArticleCreateForm.GroupPublish.class}) @ModelAttribute("form") ArticleCreateForm form,
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
			return "/article/create";
		}

		Article article = null;
		try {
			article = articleService.createArticle(form.buildArticleCreateRequest(language), errors, Post.Status.PUBLISHED, authorizedUser);
		}
		catch (BindException e) {
			if (errors.hasErrors()) {
				logger.debug("Errors: {}", errors);
				return "/article/create";
			}
			throw new RuntimeException(e);
		}
		catch (IOException e) {
			if (errors.hasErrors()) {
				logger.debug("Errors: {}", errors);
				return "/article/edit";
			}
			throw new RuntimeException(e);
		}

		redirectAttributes.addFlashAttribute("savedArticle", article);
		redirectAttributes.addAttribute("language", language);
		redirectAttributes.addAttribute("id", article.getId());
		return "redirect:/_admin/{language}/articles/describe?id={id}";
	}

	@RequestMapping(method=RequestMethod.POST, params="cancel")
	public String cancel(
			@PathVariable String language,
			@Valid @ModelAttribute("form") ArticleCreateForm form,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("language", language);
		return "redirect:/_admin/{language}/articles/";
	}
}