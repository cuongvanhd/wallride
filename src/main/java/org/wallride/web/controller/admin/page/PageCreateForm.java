package org.wallride.web.controller.admin.page;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.wallride.core.domain.Post;
import org.wallride.core.service.PageCreateRequest;
import org.wallride.web.support.DomainObjectCreateForm;

import javax.validation.constraints.NotNull;
import java.util.List;

@SuppressWarnings("serial")
public class PageCreateForm extends DomainObjectCreateForm {

	public interface GroupPublish {}
	
	private String code;

	private String coverId;

	@NotNull(groups=GroupPublish.class)
	private String title;

	@NotEmpty(groups=GroupPublish.class)
	private List<String> bodies;

	private Long authorId;
	
//	@NotNull
	@DateTimeFormat(pattern="yyyy/MM/dd")
	private LocalDateTime date;
	
	private Long parentId;

	private String metaKeywords;

	private String metaDescription;

	private Post.Status status;
	
	@NotNull
	private String language;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCoverId() {
		return coverId;
	}

	public void setCoverId(String coverId) {
		this.coverId = coverId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getBodies() {
		return bodies;
	}

	public void setBodies(List<String> bodies) {
		this.bodies = bodies;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	public String getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	public Post.Status getStatus() {
		return status;
	}

	public void setStatus(Post.Status status) {
		this.status = status;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public PageCreateRequest buildPageCreateRequest() {
		PageCreateRequest.Builder builder = new PageCreateRequest.Builder();
		return builder
				.code(code)
				.coverId(coverId)
				.title(title)
				.bodies(bodies)
				.authorId(authorId)
				.date(date)
				.parentId(parentId)
				.metaKeywords(metaKeywords)
				.metaDescription(metaDescription)
				.status(status)
				.language(language)
				.build();
	}
}