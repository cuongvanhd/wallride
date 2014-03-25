package org.wallride.core.domain;

import javax.persistence.*;
import java.io.Serializable;

/*
 * アンテナニュースフィード
 */
//@Entity
//@Table(name="antenna_items") //TODO
public class NewsItem implements Serializable {

	@Id
	private int id;

	@ManyToOne(fetch= FetchType.LAZY, optional=true)
	@JoinColumn(name="feed_id")
	private NewsFeed newsFeed;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public NewsFeed getNewsFeed() {
		return newsFeed;
	}

	public void setNewsFeed(NewsFeed newsFeed) {
		this.newsFeed = newsFeed;
	}
}
