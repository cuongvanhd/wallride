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

package org.wallride.web.controller.admin.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wallride.core.domain.Post;
import org.wallride.core.service.PostSearchRequest;
import org.wallride.core.service.PostService;
import org.wallride.web.support.DomainObjectSelectModel;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PostSelectController {

	@Inject
	private PostService postService;

	@RequestMapping(value="/{language}/posts/select")
	public @ResponseBody List<DomainObjectSelectModel> select(
			@PathVariable String language,
			@RequestParam(required=false) String keyword) {
		PostSearchRequest request = new PostSearchRequest(language)
				.withStatus(Post.Status.PUBLISHED)
				.withKeyword(keyword);
		Page<Post> posts = postService.readPosts(request, new PageRequest(0, 30));

		List<DomainObjectSelectModel> results = new ArrayList<>();
		if (posts.hasContent()) {
			for (Post post : posts) {
				DomainObjectSelectModel model = new DomainObjectSelectModel(post);
				results.add(model);
			}
		}
		return results;
	}

	@RequestMapping(value="/{language}/posts/select/{id}")
	public @ResponseBody DomainObjectSelectModel select(
			@PathVariable String language,
			@RequestParam Long id,
			HttpServletResponse response)
			throws IOException {
		Post post = postService.readPostById(id, language);
		if (post == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		DomainObjectSelectModel model = new DomainObjectSelectModel(post);
		return model;
	}
}
