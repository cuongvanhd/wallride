package org.wallride.admin.web.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.wallride.admin.service.PageService;
import org.wallride.core.domain.Page;
import org.wallride.core.web.DomainObjectDescribeController;

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
			@RequestParam(required=false) String token,
			Model model,
			HttpSession session) {
		return super.requestMappingDescribe(id, token, model, session);
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
		return "/page/describe";
	}

	@Override
	protected Page readDomainObject(long id) {
		return pageService.readPageById(id);
	}
}