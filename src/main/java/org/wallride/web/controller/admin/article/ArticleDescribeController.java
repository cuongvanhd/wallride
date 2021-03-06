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

package org.wallride.web.controller.admin.article;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.wallride.core.domain.Article;
import org.wallride.core.service.ArticleService;
import org.wallride.web.support.DomainObjectDescribeController;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value="/{language}/articles/describe", method=RequestMethod.GET)
public class ArticleDescribeController extends DomainObjectDescribeController<Article, ArticleSearchForm> {

	@Inject
	private ArticleService articleService;

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

	@RequestMapping(params = "part=delete-form")
	public String partDeleteForm(
			@PathVariable String language,
			@RequestParam long id, Model model) {
		Article article = articleService.readArticleById(id, language);
		model.addAttribute("article", article);
		return "article/describe::delete-form";
	}

	@Override
	protected Class<ArticleSearchForm> getDomainObjectSearchFormClass() {
		return ArticleSearchForm.class;
	}

	@Override
	protected String getModelAttributeName() {
		return "article";
	}

	@Override
	protected String getViewName() {
		return "article/describe";
	}

	@Override
	protected Article readDomainObject(long id) {
		return articleService.readArticleById(id, LocaleContextHolder.getLocale().getLanguage());
	}

	@Override
	protected Page<Article> readDomainObjects(ArticleSearchForm form, Pageable pageable) {
		return articleService.readArticles(form.toArticleSearchRequest(), pageable);
	}
}