package org.wallride.admin.web.banner;

import org.hibernate.validator.constraints.URL;
import org.wallride.core.domain.Banner;
import org.wallride.core.web.DomainObjectCreateForm;

import javax.validation.constraints.NotNull;

@SuppressWarnings("serial")
public class BannerCreateForm extends DomainObjectCreateForm {

	@NotNull
	private String language;

	@NotNull
	private Banner.Type type;

	@NotNull
	private String imageId;

	private String title;

	@URL
	private String link;

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
}
