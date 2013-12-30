package org.wallride.admin.web.banner;

import org.hibernate.validator.constraints.URL;
import org.springframework.beans.BeanUtils;
import org.wallride.core.domain.Banner;
import org.wallride.core.web.DomainObjectEditForm;

import javax.validation.constraints.NotNull;

@SuppressWarnings("serial")
public class BannerEditForm extends DomainObjectEditForm {

	@NotNull
	private Long id;

	@NotNull
	private String language;

	@NotNull
	private Banner.Type type;

	@NotNull
	private String imageId;

	private String title;

	@URL
	private String link;

	private boolean linkTargetBlank;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Banner.Type getType() {
		return type;
	}

	public void setType(Banner.Type type) {
		this.type = type;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
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

	public boolean isLinkTargetBlank() {
		return linkTargetBlank;
	}

	public void setLinkTargetBlank(boolean linkTargetBlank) {
		this.linkTargetBlank = linkTargetBlank;
	}

	public static BannerEditForm fromDomainObject(Banner banner) {
		BannerEditForm form = new BannerEditForm();
		BeanUtils.copyProperties(banner, form);
		form.setImageId(banner.getImage() != null ? banner.getImage().getId() : null);
		return form;
	}
}
