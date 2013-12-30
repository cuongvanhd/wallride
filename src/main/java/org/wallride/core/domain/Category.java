package org.wallride.core.domain;

import org.hibernate.annotations.*;
import org.hibernate.search.annotations.Field;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
@Table(name="category", uniqueConstraints=@UniqueConstraint(columnNames={"code", "language"}))
@DynamicInsert
@DynamicUpdate
@SuppressWarnings("serial")
public class Category extends DomainObject<Long> implements Comparable<Category> {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@Column(length=200, nullable=false)
	@Field
	private String code;

	@Column(length=3, nullable=false)
	@Field
	private String language;

	@Column(length=200, nullable=false)
	@Field
	private String name;

	@Lob
	@Field
	private String description;

	@Column(name="lft", nullable=false)
	@Field
	private int lft;

	@Column(name="rgt", nullable=false)
	@Field
	private int rgt;

//	@Column(nullable=false)
//	private int depth;
//
//	@Column(nullable=false)
//	private int sort;

	@ManyToOne
	private Category parent;

	@OneToMany(mappedBy="parent", cascade=CascadeType.ALL)
	private List<Category> children;

	@ManyToMany
	@JoinTable(
			name="article_category",
			joinColumns={@JoinColumn(name="category_id")},
			inverseJoinColumns=@JoinColumn(name="article_id", referencedColumnName="id"))
	@Sort(type= SortType.NATURAL)
	private SortedSet<Article> articles = new TreeSet<>();

	@Formula("(" +
			"select count(distinct article.id) from article article " +
			"inner join post post on article.id = post.id " +
			"inner join article_category category on article.id = category.article_id " +
			"where category.category_id = id " +
			"and post.status = 'PUBLISHED') ")
	private int articleCount;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLft() {
		return lft;
	}

	public void setLft(int lft) {
		this.lft = lft;
	}

	public int getRgt() {
		return rgt;
	}

	public void setRgt(int rgt) {
		this.rgt = rgt;
	}

	//	public int getDepth() {
//		return depth;
//	}
//
//	public void setDepth(int depth) {
//		this.depth = depth;
//	}
//
//	public int getSort() {
//		return sort;
//	}
//
//	public void setSort(int sort) {
//		this.sort = sort;
//	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public List<Category> getChildren() {
		return children;
	}

	public void setChildren(List<Category> children) {
		this.children = children;
	}

	public SortedSet<Article> getArticles() {
		return articles;
	}

	public void setArticles(SortedSet<Article> articles) {
		this.articles = articles;
	}

	public int getArticleCount() {
		return articleCount;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int compareTo(Category category) {
		int lftDiff = getLft() - category.getLft();
		if (lftDiff != 0) {
			return lftDiff;
		}
		return (int) (category.getId() - getId());
	}
}
