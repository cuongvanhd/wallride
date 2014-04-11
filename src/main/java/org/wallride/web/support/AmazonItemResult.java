package org.wallride.web.support;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="result")
public class AmazonItemResult {

	private String url;

	private String name;

	private String image;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
