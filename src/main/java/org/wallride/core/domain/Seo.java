package org.wallride.core.domain;

import org.hibernate.search.annotations.Field;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.io.Serializable;

@Embeddable
@SuppressWarnings("serial")
public class Seo implements Serializable {

	@Field
	private String title;

	@Lob
	@Field
	private String description;

	@Lob
	@Field
	private String keywords;

	@Lob
	@Field
	private String ogTitle;

	@Lob
	@Field
	private String ogImage;

	@Lob
	@Field
	private String ogUrl;

	@Lob
	@Field
	private String ogSiteName;

	@Lob
	@Field
	private String ogDescription;

	private String ogAppId;

	private String ogType;

	private String ogLocale;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getOgTitle() {
		return ogTitle;
	}

	public void setOgTitle(String ogTitle) {
		this.ogTitle = ogTitle;
	}

	public String getOgImage() {
		return ogImage;
	}

	public void setOgImage(String ogImage) {
		this.ogImage = ogImage;
	}

	public String getOgUrl() {
		return ogUrl;
	}

	public void setOgUrl(String ogUrl) {
		this.ogUrl = ogUrl;
	}

	public String getOgSiteName() {
		return ogSiteName;
	}

	public void setOgSiteName(String ogSiteName) {
		this.ogSiteName = ogSiteName;
	}

	public String getOgDescription() {
		return ogDescription;
	}

	public void setOgDescription(String ogDescription) {
		this.ogDescription = ogDescription;
	}

	public String getOgAppId() {
		return ogAppId;
	}

	public void setOgAppId(String ogAppId) {
		this.ogAppId = ogAppId;
	}

	public String getOgType() {
		return ogType;
	}

	public void setOgType(String ogType) {
		this.ogType = ogType;
	}

	public String getOgLocale() {
		return ogLocale;
	}

	public void setOgLocale(String ogLocale) {
		this.ogLocale = ogLocale;
	}
}
