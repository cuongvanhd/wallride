package org.wallride.admin.web;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Controller
@RequestMapping("/{language}/system-status")
public class SystemStatusController {

	@Inject
	private Environment environment;

	@PersistenceContext
	private EntityManager entityManager;

	@RequestMapping
	public String status(Model model) {
		model.addAttribute("environment", environment);
		model.addAttribute("sysprops", System.getProperties());
		return "/system-status";
	}

	@RequestMapping(method= RequestMethod.POST, params="indexing")
	public String indexing(RedirectAttributes redirectAttributes) {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
//		fullTextEntityManager.createIndexer().startAndWait();
		fullTextEntityManager.createIndexer().start();
		redirectAttributes.addFlashAttribute("indexed", true);
		return "redirect:/_admin/{language}/system-status";
	}
}
