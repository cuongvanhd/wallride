package org.wallride.core.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="post", uniqueConstraints=@UniqueConstraint(columnNames={"code", "language"}))
@Inheritance(strategy= InheritanceType.JOINED)
@DynamicInsert
@DynamicUpdate
public class Post extends DomainObject<Long> {

	public enum Status {
		DRAFT, SCHEDULED, PUBLISHED
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

	@Lob
	@Field
	private String body;

	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	@Field
	@FieldBridge(impl=LocalDateTimeBridge.class)
	private LocalDateTime date;

	@ManyToOne
	private User author;

	@Enumerated(EnumType.STRING)
	@Column(length=50, nullable=false)
	@Field
	private Status status;

	@ManyToMany
	@JoinTable(name="post_media", joinColumns=@JoinColumn(name="post_id", referencedColumnName="id"), inverseJoinColumns=@JoinColumn(name="media_id", referencedColumnName="id"))
	@IndexColumn(name="`index`")
	private List<Media> medias;

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

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
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

	@Override
	public String toString() {
		return getTitle();
	}
}
