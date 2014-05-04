package org.wallride.core.domain;

import org.hibernate.annotations.*;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

@Entity
@Table(name="article")
@PrimaryKeyJoinColumn
@DynamicInsert
@DynamicUpdate
@Analyzer(definition="synonyms")
@Indexed
@SuppressWarnings("serial")
public class Article extends Post implements Comparable<Article> {

	@ManyToMany
	@JoinTable(
			name="article_category",
			joinColumns={@JoinColumn(name="article_id")},
			inverseJoinColumns=@JoinColumn(name="category_id", referencedColumnName="id"))
	@Sort(type=SortType.NATURAL)
	@IndexedEmbedded
	private SortedSet<Category> categories = new TreeSet<>();

	@ManyToMany
	@JoinTable(
			name="article_tag",
			joinColumns={@JoinColumn(name="article_id")},
			inverseJoinColumns=@JoinColumn(name="tag_id", referencedColumnName="id"))
	@Sort(type=SortType.NATURAL)
	@IndexedEmbedded
	private SortedSet<Tag> tags = new TreeSet<>();

	@ManyToMany
	@JoinTable(name="article_related_article",
			joinColumns = { @JoinColumn(name="article_id")},
			inverseJoinColumns = { @JoinColumn(name="related_article_id") })
	private Set<Article> relatedArticles = new HashSet<>();

//	@OneToMany(mappedBy="article", fetch=FetchType.LAZY)
//	private Set<ArticleLink> links = new HashSet<>();

	public SortedSet<Category> getCategories() {
		return categories;
	}

	public void setCategories(SortedSet<Category> categories) {
		this.categories = categories;
	}

	public SortedSet<Tag> getTags() {
		return tags;
	}

	public void setTags(SortedSet<Tag> tags) {
		this.tags = tags;
	}

	public Set<Article> getRelatedArticles() {
		return relatedArticles;
	}

	public void setRelatedArticles(Set<Article> relatedArticles) {
		this.relatedArticles = relatedArticles;
	}

//	public Set<ArticleLink> getLinks() {
//		return links;
//	}
//
//	public void setLinks(Set<ArticleLink> links) {
//		this.links = links;
//	}

	public int compareTo(Article article) {
		if (getDate() != null && article.getDate() == null) return 1;
		if (getDate() == null && article.getDate() != null) return -1;
		if (getDate() != null && article.getDate() != null) {
			int r = getDate().compareTo(article.getDate());
			if (r != 0) return r * -1;
		}
		return (int) (article.getId() - getId());
	}
}
