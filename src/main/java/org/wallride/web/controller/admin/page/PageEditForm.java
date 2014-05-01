package org.wallride.web.controller.admin.page;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.wallride.core.domain.Page;
import org.wallride.core.domain.Post;
import org.wallride.core.domain.PostBody;
import org.wallride.core.service.PageUpdateRequest;
import org.wallride.web.support.DomainObjectEditForm;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class PageEditForm extends DomainObjectEditForm {
	
	interface GroupPublish {}
	
	@NotNull
	private Long id;

	private String code;

	private String coverId;

	@NotNull(groups=GroupPublish.class)
	private String title;

	@NotEmpty(groups=GroupPublish.class)
	private List<String> bodies;

	private Long authorId;
	
//	@NotNull
	@DateTimeFormat(pattern="yyyy/MM/dd HH:mm")
	private LocalDateTime date;

	private Long parentId;

	private String metaKeywords;

	private String metaDescription;

	private Post.Status status;
	
	@NotNull
	private String language;

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

	public PageUpdateRequest buildPageUpdateRequest() {
		PageUpdateRequest.Builder builder = new PageUpdateRequest.Builder();
		return builder
				.id(id)
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

	public static PageEditForm fromDomainObject(Page page) {
		PageEditForm form = new PageEditForm();
		BeanUtils.copyProperties(page, form);
		form.setCoverId(page.getCover() != null ? page.getCover().getId() : null);
		List<PostBody> postBodies = page.getBodies();
		List<String> bodies = new ArrayList<>();
		for (PostBody body : postBodies) {
			bodies.add(body.getBody());
		}
		form.setBodies(bodies);
		form.setParentId(page.getParent() != null ? page.getParent().getId() : null);
		if (page.getSeo() != null) {
			form.setMetaKeywords(page.getSeo().getKeywords());
			form.setMetaDescription(page.getSeo().getDescription());
		}
		return form;
	}
}
