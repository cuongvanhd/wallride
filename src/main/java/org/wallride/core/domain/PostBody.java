package org.wallride.core.domain;

import org.hibernate.annotations.Parent;
import org.hibernate.search.annotations.Field;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.io.Serializable;

@Embeddable
public class PostBody implements Serializable {

	@Parent
	private Post post;

	@Lob
	@Field
	private String body;

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
