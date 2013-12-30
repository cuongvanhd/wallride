package org.wallride.core.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;

@Entity
@Table(name="banner")
@DynamicInsert
@DynamicUpdate
public class Banner extends DomainObject<Long> implements Comparable<Banner> {

	public enum Type {
		MAIN, SUB, ASIDE
	}

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;

	@Column(length=200)
	private String title;

	@Column(length=500)
	private String link;

	@Column(nullable=false)
	private int sort = -1;

	@ManyToOne
	private Media image;

	@Enumerated(EnumType.STRING)
	@Column(length=50, nullable=false)
	@Field
	private Type type;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public Media getImage() {
		return image;
	}

	public void setImage(Media image) {
		this.image = image;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public int compareTo(Banner banner) {
		int sort = getSort() - banner.getSort();
		if (sort != 0) {
			return sort;
		}
		return (int) (banner.getId() - getId());
	}
}