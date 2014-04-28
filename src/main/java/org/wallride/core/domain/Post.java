package org.wallride.core.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="post", uniqueConstraints=@UniqueConstraint(columnNames={"code", "language"}))
@Inheritance(strategy= InheritanceType.JOINED)
@DynamicInsert
@DynamicUpdate
public class Post extends DomainObject<Long> {

	public enum Status {
		DRAFT, SCHEDULED, PUBLISHED, UNPUBLISHED
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@Column(length=200)
	@Field
	private String code;

	@Column(length=3, nullable=false)
	@Field
	private String language;

	@Column(length=200)
	@Field
	private String title;

	@ManyToOne
	private Media cover;

//	@Lob
//	@Field
//	private String body;

	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	@Field
	@FieldBridge(impl=LocalDateTimeBridge.class)
	private LocalDateTime date;

	@ManyToOne
	private User author;

	@Column(name="author_name")
	@Field
	private String authorName;

	@Enumerated(EnumType.STRING)
	@Column(length=50, nullable=false)
	@Field
	private Status status;

	@ElementCollection(fetch=FetchType.LAZY)
	@JoinTable(name="post_body", joinColumns=@JoinColumn(name="post_id"))
	@OrderColumn(name="`index`")
	@IndexedEmbedded
	private List<PostBody> bodies = new ArrayList<>();

	@ManyToMany
	@JoinTable(name="post_media", joinColumns=@JoinColumn(name="post_id", referencedColumnName="id"), inverseJoinColumns=@JoinColumn(name="media_id", referencedColumnName="id"))
	@IndexColumn(name="`index`")
	private List<Media> medias;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name="title", column=@Column(name="seo_title", length=1000)),
			@AttributeOverride(name="description", column=@Column(name="seo_description")),
			@AttributeOverride(name="keywords", column=@Column(name="seo_keywords")),
			@AttributeOverride(name="ogTitle", column=@Column(name="og_title")),
			@AttributeOverride(name="ogImage", column=@Column(name="og_image")),
			@AttributeOverride(name="ogUrl", column=@Column(name="og_url")),
			@AttributeOverride(name="ogSiteName", column=@Column(name="og_sitename")),
			@AttributeOverride(name="ogDescription", column=@Column(name="og_description")),
			@AttributeOverride(name="ogAppId", column=@Column(name="og_app_id")),
			@AttributeOverride(name="ogType", column=@Column(name="og_type")),
			@AttributeOverride(name="ogLocale", column=@Column(name="og_locale")),
	})
	@IndexedEmbedded
	private Seo seo = new Seo();

	@Override
	public Long getId() {
		return id;
	}

	public void setId(long id) {
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Media getCover() {
		return cover;
	}

	public void setCover(Media cover) {
		this.cover = cover;
	}

//	public String getBody() {
//		return body;
//	}
//
//	public void setBody(String body) {
//		this.body = body;
//	}


	public List<PostBody> getBodies() {
		return bodies;
	}

	public void setBodies(List<PostBody> bodies) {
		this.bodies = bodies;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<Media> getMedias() {
		return medias;
	}

	public void setMedias(List<Media> medias) {
		this.medias = medias;
	}

	public Seo getSeo() {
		return seo;
	}

	public void setSeo(Seo seo) {
		this.seo = seo;
	}

	@Override
	public String toString() {
		return getTitle();
	}
}
