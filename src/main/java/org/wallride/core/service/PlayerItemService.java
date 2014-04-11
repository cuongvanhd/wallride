package org.wallride.core.service;

import org.springframework.stereotype.Service;
import org.wallride.web.support.AmazonItemClient;
import org.wallride.web.support.AmazonItemResult;

import java.util.List;

@Service
public class PlayerItemService {

	private static final String AMAZON_URL = "http://ecs.amazonaws.com/onca/xml";
	private static final String AMAZON_ACCESS_KEY = "AKIAIVJIXWFF3PRWPYVA";
	private static final String AMAZON_SECRET_ACCESS_KEY = "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";

	public List<AmazonItemResult> readItems() {
		AmazonItemClient client = new AmazonItemClient(AMAZON_ACCESS_KEY, AMAZON_SECRET_ACCESS_KEY, AMAZON_URL);
		String keyword = "aerosmith";
		List<AmazonItemResult> result = client.executeItemSearch(keyword);
		return result;
	}
}
