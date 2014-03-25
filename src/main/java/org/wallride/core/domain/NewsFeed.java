package org.wallride.core.domain;


import javax.persistence.*;
import java.util.List;

/*
 * アンテナニュースフィード
 */
//@Entity
//@Table(name="antenna_feeds") //TODO
public class NewsFeed {

	@Id
	private int id;

	@OneToMany(mappedBy="league", fetch=FetchType.LAZY, cascade= CascadeType.ALL)
	private List<NewsItem> newsItems;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<NewsItem> getNewsItems() {
		return newsItems;
	}

	public void setNewsItems(List<NewsItem> newsItems) {
		this.newsItems = newsItems;
	}
}
