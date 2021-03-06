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

package org.wallride.web.controller.admin.tag;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.wallride.core.domain.Tag;
import org.wallride.core.service.TagService;
import org.wallride.web.support.DomainObjectSelectModel;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TagSelectController {

	@Inject
	private TagService tagService;

	@RequestMapping(value="/{language}/tags/select")
	public @ResponseBody List<DomainObjectSelectModel> select(
			@PathVariable String language,
			@RequestParam(required=false) String keyword) {
		TagSearchForm form = new TagSearchForm();
		form.setKeyword(keyword);
		form.setLanguage(language);
		Page<Tag> tags = tagService.readTags(form.buildTagSearchRequest());

		List<DomainObjectSelectModel> results = new ArrayList<>();
		if (tags.hasContent()) {
			for (Tag tag : tags) {
				DomainObjectSelectModel model = new DomainObjectSelectModel(tag.getId(), tag.getName());
				results.add(model);
			}
		}
		return results;
	}

	@RequestMapping(value="/{language}/tags/select/{id}", method= RequestMethod.GET)
	public @ResponseBody DomainObjectSelectModel select(
			@PathVariable String language,
			@PathVariable Long id,
			HttpServletResponse response) throws IOException {
		Tag tag = tagService.readTagById(id, language);
		if (tag == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		DomainObjectSelectModel model = new DomainObjectSelectModel(tag.getName(), tag.getName());
		return model;
	}
}
