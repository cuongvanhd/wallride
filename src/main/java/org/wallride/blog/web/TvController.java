package org.wallride.blog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/{language}/tv")
public class TvController {

	@RequestMapping
	public String youtube() {
		return "/tv";
	}
}
