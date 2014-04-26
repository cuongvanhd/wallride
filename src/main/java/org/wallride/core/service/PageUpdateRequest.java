package org.wallride.core.service;

import org.joda.time.LocalDateTime;
import org.wallride.core.domain.Post;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class PageUpdateRequest implements Serializable {
	
	private Long id;
	private String code;
	private String coverId;
	private String title;
	private List<String> bodies = new ArrayList<>();
	private Long authorId;
	private LocalDateTime date;
	private Long parentId;
	private String metaKeywords;
	private String metaDescription;
	private Post.Status status;
	private String language;

	public Long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getCoverId() {
		return coverId;
	}

	public String getTitle() {
		return title;
	}

	public List<String> getBodies() {
		return bodies;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public Long getParentId() {
		return parentId;
	}

	public String getMetaKeywords() {
		return metaKeywords;
	}

	public String getMetaDescription() {
		return metaDescription;
	}

	public Post.Status getStatus() {
		return status;
	}

	public String getLanguage() {
		return language;
	}

	public static class Builder  {

		private Long id;
		private String code;
		private String coverId;
		private String title;
		private List<String> bodies;
		private Long authorId;
		private LocalDateTime date;
		private Long parentId;
		private String metaKeywords;
		private String metaDescription;
		private Post.Status status;
		private String language;

		public Builder() {
		}

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder code(String code) {
			this.code = code;
			return this;
		}

		public Builder coverId(String coverId) {
			this.coverId = coverId;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder bodies(List<String> bodies) {
			this.bodies = bodies;
			return this;
		}

		public Builder authorId(Long authorId) {
			this.authorId = authorId;
			return this;
		}

		public Builder date(LocalDateTime date) {
			this.date = date;
			return this;
		}

		public Builder parentId(Long parentId) {
			this.parentId = parentId;
			return this;
		}

		public Builder metaKeywords(String metaKeywords) {
			this.metaKeywords = metaKeywords;
			return this;
		}

		public Builder metaDescription(String metaDescription) {
			this.metaDescription = metaDescription;
			return this;
		}

		public Builder status(Post.Status status) {
			this.status = status;
			return this;
		}

		public Builder language(String language) {
			this.language = language;
			return this;
		}

		public PageUpdateRequest build() {
			PageUpdateRequest request = new PageUpdateRequest();
			request.id = id;
			request.code = code;
			request.coverId = coverId;
			request.title = title;
			request.bodies = bodies;
			request.authorId = authorId;
			request.date = date;
			request.parentId = parentId;
			request.metaKeywords = metaKeywords;
			request.metaDescription = metaDescription;
			request.status = status;
			request.language = language;
			return request;
		}
	}
}
