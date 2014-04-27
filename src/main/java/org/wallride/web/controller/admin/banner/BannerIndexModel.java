package org.wallride.web.controller.admin.banner;

import org.wallride.core.domain.Banner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BannerIndexModel extends ArrayList<Map<String, Object>> {

	public BannerIndexModel(List<Banner> banners) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		for (Banner banner : banners) {
			Map<String, Object> nodes = new LinkedHashMap<>();
			nodes.put("id", banner.getId());
			nodes.put("type", banner.getType());
			nodes.put("title", banner.getTitle());
			nodes.put("link", banner.getLink());
			result.add(nodes);
		}
	}
}
