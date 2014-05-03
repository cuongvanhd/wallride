package org.wallride.web.controller.admin.article;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.wallride.core.domain.*;
import org.wallride.core.service.ArticleUpdateRequest;
import org.wallride.web.support.DomainObjectEditForm;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
public class ArticleEditForm extends DomainObjectEditForm {

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

	@DateTimeFormat(pattern="yyyy/MM/dd HH:mm")
	private LocalDateTime date;

	private Set<Long> categoryIds = new HashSet<>();

	private Set<Long> relatedArticleIds = new HashSet<>();

	private String tags;

	private String metaKeywords;

	private String metaDescription;

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

	public Set<Long> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(Set<Long> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public Set<Long> getRelatedArticleIds() {
		return relatedArticleIds;
	}

	public void setRelatedArticleIds(Set<Long> relatedArticleIds) {
		this.relatedArticleIds = relatedArticleIds;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public ArticleUpdateRequest buildArticleUpdateRequest() {
		ArticleUpdateRequest.Builder builder = new ArticleUpdateRequest.Builder();
		return builder
				.id(id)
				.code(code)
				.coverId(coverId)
				.title(title)
				.bodies(bodies)
				.authorId(authorId)
				.date(date)
				.categoryIds(categoryIds)
				.relatedArticleIds(relatedArticleIds)
				.tags(tags)
				.metaKeywords(metaKeywords)
				.metaDescription(metaDescription)
				.language(language)
				.build();
	}

	public static ArticleEditForm fromDomainObject(Article article) {
		ArticleEditForm form = new ArticleEditForm();
		BeanUtils.copyProperties(article, form);
		if (article.getSeo() != null) {
			form.setMetaKeywords(article.getSeo().getKeywords());
			form.setMetaDescription(article.getSeo().getDescription());
		}
		form.setCoverId(article.getCover() != null ? article.getCover().getId() : null);
		List<PostBody> postBodies = article.getBodies();
		List<String> bodies = new ArrayList<>();
		for (PostBody body : postBodies) {
			bodies.add(body.getBody());
		}
		form.setBodies(bodies);
		for (Category category : article.getCategories()) {
			form.getCategoryIds().add(category.getId());
		}
		for (Article relatedArticle : article.getRelatedArticles()) {
			form.getRelatedArticleIds().add(relatedArticle.getId());
		}
		List<Long> tagList = new ArrayList<>();
		for (Tag tag : article.getTags()) {
			tagList.add(tag.getId());
		}
		form.setTags(StringUtils.join(tagList, ","));
		return form;
	}
}
