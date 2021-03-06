/*
 * Copyright 2014 Tagbangers, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wallride.web.controller.admin.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wallride.core.domain.Page;
import org.wallride.core.domain.PageTree;
import org.wallride.core.domain.Post;
import org.wallride.core.service.DuplicateCodeException;
import org.wallride.core.service.EmptyCodeException;
import org.wallride.core.service.PageService;
import org.wallride.core.support.AuthorizedUser;
import org.wallride.web.support.DomainObjectSavedModel;
import org.wallride.web.support.RestValidationErrorModel;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.groups.Default;

@Controller
@RequestMapping("/{language}/pages/create")
public class PageCreateController {

	private static Logger logger = LoggerFactory.getLogger(PageCreateController.class);

	@Inject
	private PageService pageService;

	@Inject
	private MessageSourceAccessor messageSourceAccessor;

	@ModelAttribute("form")
	public PageCreateForm pageCreateForm() {
		return new PageCreateForm();
	}

	@ModelAttribute("pageTree")
	public PageTree pageTree(@PathVariable String language) {
		return pageService.readPageTree(language);
	}

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody RestValidationErrorModel bindException(BindException e) {
		logger.debug("BindException", e);
		return RestValidationErrorModel.fromBindingResult(e.getBindingResult(), messageSourceAccessor);
	}

	@RequestMapping(method=RequestMethod.GET)
	public String create() {
		return "page/create";
	}

	@RequestMapping(method=RequestMethod.GET, params="part=page-fieldset")
	public String partPageFieldset(@PathVariable String language, Model model) {
		PageTree pageTree = pageService.readPageTree(language);
		model.addAttribute("pageTree", pageTree);
		return "page/create::#page-fieldset";
	}

	@RequestMapping(method=RequestMethod.POST, params="draft")
	public @ResponseBody DomainObjectSavedModel saveAsDraft(
			@PathVariable String language,
			@Validated @ModelAttribute("form") PageCreateForm form,
			BindingResult errors,
			AuthorizedUser authorizedUser,
			RedirectAttributes redirectAttributes)
			throws BindException {
		if (errors.hasErrors()) {
			for (ObjectError error : errors.getAllErrors()) {
				if (!"validation.NotNull".equals(error.getCode())) {
					throw new BindException(errors);
				}
			}
		}

		Page page = null;
		try {
			page = pageService.createPage(form.buildPageCreateRequest(), Post.Status.DRAFT, authorizedUser);
		}
		catch (EmptyCodeException e) {
			errors.rejectValue("code", "NotNull");
		}
		catch (DuplicateCodeException e) {
			errors.rejectValue("code", "NotDuplicate");
		}
		if (errors.hasErrors()) {
			logger.debug("Errors: {}", errors);
			throw new BindException(errors);
		}

		return new DomainObjectSavedModel<>(page);
	}

	@RequestMapping(method=RequestMethod.POST, params="publish")
	public String saveAsPublished(
			@PathVariable String language,
			@Validated({Default.class, PageCreateForm.GroupPublish.class}) @ModelAttribute("form") PageCreateForm form,
			BindingResult errors,
			AuthorizedUser authorizedUser,
			RedirectAttributes redirectAttributes) {
		if (errors.hasErrors()) {
			return "page/create";
		}

		Page page = null;
		try {
			page = pageService.createPage(form.buildPageCreateRequest(), Post.Status.PUBLISHED, authorizedUser);
		}
		catch (EmptyCodeException e) {
			errors.rejectValue("code", "NotNull");
		}
		catch (DuplicateCodeException e) {
			errors.rejectValue("code", "NotDuplicate");
		}
		if (errors.hasErrors()) {
			logger.debug("Errors: {}", errors);
			return "page/create";
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
		return "redirect:/_admin/{language}/pages/";
	}
}