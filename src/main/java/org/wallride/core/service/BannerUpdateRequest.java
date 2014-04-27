package org.wallride.core.service;

import org.wallride.core.domain.Banner;

import java.io.Serializable;

public class BannerUpdateRequest implements Serializable {

	private Long id;
	private String language;
	private Banner.Type type;
	private String imageId;
	private String title;
	private String link;
	private boolean linkTargetBlank;

	public Long getId() {
		return id;
	}

	public String getLanguage() {
		return language;
	}

	public Banner.Type getType() {
		return type;
	}

	public String getImageId() {
		return imageId;
	}

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}

	public boolean isLinkTargetBlank() {
		return linkTargetBlank;
	}

	public static class Builder {

		private Long id;
		private String language;
		private Banner.Type type;
		private String imageId;
		private String title;
		private String link;
		private boolean linkTargetBlank;

		public Builder() {}

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder language(String language) {
			this.language = language;
			return this;
		}

		public Builder type(Banner.Type type) {
			this.type = type;
			return this;
		}

		public Builder imageId(String imageId) {
			this.imageId = imageId;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder link(String link) {
			this.link = link;
			return this;
		}

		public Builder linkTargetBlank(boolean linkTargetBlank) {
			this.linkTargetBlank = linkTargetBlank;
			return this;
		}

		public BannerUpdateRequest build() {
			BannerUpdateRequest request = new BannerUpdateRequest();
			request.id = id;
			request.language = language;
			request.type = type;
			request.imageId = imageId;
			request.title = title;
			request.link = link;
			request.linkTargetBlank = linkTargetBlank;
			return request;
		}
	}
}