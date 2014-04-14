package org.wallride.core.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;

@Entity
@Table(name="article_link")
@DynamicInsert
@DynamicUpdate
public class ArticleLink extends DomainObject<Long> {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@Column(length=3, nullable=false)
	@Field
	private String language;

	@Lob
	@Column(nullable=false)
	@Field
	private String title;

	@Lob
	@Column(nullable=false)
	@Field
	private String url;

	@ManyToOne
	private Article article;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	@Override
	public String toString() {
		return getTitle();
	}
}
