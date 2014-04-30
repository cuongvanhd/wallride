package org.wallride.web.controller.admin.tag;

import org.springframework.context.i18n.LocaleContextHolder;
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

	@RequestMapping(value="/{language}/tags/select-search")
	public @ResponseBody List<DomainObjectSelectModel> select(@RequestParam(required=false) String keyword) {
		TagSearchForm form = new TagSearchForm();
		form.setKeyword(keyword);
		Page<Tag> paginator = tagService.readTags(form.buildTagSearchRequest());

		List<DomainObjectSelectModel> results = new ArrayList<>();
		if (paginator.hasContent()) {
			for (Tag tag : paginator) {
				DomainObjectSelectModel model = new DomainObjectSelectModel(tag);
				results.add(model);
			}
		}
		return results;
	}

	@RequestMapping(value="/{language}/tags/select", method= RequestMethod.GET)
	public @ResponseBody DomainObjectSelectModel select(@RequestParam Long id, HttpServletResponse response) throws IOException {
		Tag tag = tagService.readTagById(id, LocaleContextHolder.getLocale().getLanguage());
		if (tag == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		DomainObjectSelectModel model = new DomainObjectSelectModel(tag);
		return model;
	}
}
