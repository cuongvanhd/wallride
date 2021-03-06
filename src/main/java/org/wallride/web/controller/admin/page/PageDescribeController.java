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

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.wallride.core.domain.Page;
import org.wallride.core.service.PageService;
import org.wallride.web.support.DomainObjectDescribeController;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value="/{language}/pages/describe", method=RequestMethod.GET)
public class PageDescribeController extends DomainObjectDescribeController<Page, PageSearchForm> {

	@Inject
	private PageService pageService;

	@RequestMapping
	public String describe(
			@RequestParam long id,
			Model model,
			HttpSession session) {
		return super.requestMappingDescribe(id, null, model, session);
	}

	@RequestMapping(params = "pageable")
	public String describe(
			@RequestParam long id,
			@PageableDefault(50) Pageable pageable,
			Model model,
			HttpSession session) {
		return super.requestMappingDescribe(id, pageable, model, session);
	}

	@RequestMapping(params="part=delete-form")
	public String partDeleteDialog(
			@PathVariable String language,
			@RequestParam long id, Model model) {
		Page page = pageService.readPageById(id, language);
		model.addAttribute("page", page);
		return "page/describe::delete-form";
	}

	@Override
	protected Class<PageSearchForm> getDomainObjectSearchFormClass() {
		return PageSearchForm.class;
	}

	@Override
	protected String getModelAttributeName() {
		return "page";
	}

	@Override
	protected String getViewName() {
		return "page/describe";
	}

	@Override
	protected Page readDomainObject(long id) {
		return pageService.readPageById(id, LocaleContextHolder.getLocale().getLanguage());
	}

	@Override
	protected org.springframework.data.domain.Page<Page> readDomainObjects(PageSearchForm form, Pageable pageable) {
		return pageService.readPages(form.toPageSearchRequest(), pageable);
	}
}