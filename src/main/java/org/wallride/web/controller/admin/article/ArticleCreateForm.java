package org.wallride.web.controller.admin.article;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;
import org.wallride.core.service.ArticleCreateRequest;
import org.wallride.web.support.DomainObjectCreateForm;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
public class ArticleCreateForm extends DomainObjectCreateForm {

	interface GroupPublish {}

	private String code;

	private String coverId;

	@NotNull(groups=GroupPublish.class)
	private String title;

	@NotEmpty(groups=GroupPublish.class)
	private String[] bodies;

	private Long authorId;

//	@NotNull
	@DateTimeFormat(pattern="yyyy/MM/dd")
	private LocalDateTime date;

	private Set<Long> categoryIds = new HashSet<>();

	private Set<Long> tagIds = new HashSet<>();

	private String metaKeywords;

	private String metaDescription;

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

	public String[] getBodies() {
		return bodies;
	}

	public void setBodies(String[] bodies) {
		this.bodies = bodies;
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

	public Set<Long> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(Set<Long> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public Set<Long> getTagIds() {
		return tagIds;
	}

	public void setTagIds(Set<Long> tagIds) {
		this.tagIds = tagIds;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public ArticleCreateRequest buildArticleCreateRequest() {
		List<String> bodyList = (List<String>)CollectionUtils.arrayToList(bodies);
		ArticleCreateRequest.Builder builder = new ArticleCreateRequest.Builder();
		return builder
				.code(code)
				.coverId(coverId)
				.title(title)
				.bodies(bodyList)
				.authorId(authorId)
				.date(date)
				.categoryIds(categoryIds)
				.metaKeywords(metaKeywords)
				.metaDescription(metaDescription)
				.tagIds(tagIds)
				.language(language)
				.build();
	}
}