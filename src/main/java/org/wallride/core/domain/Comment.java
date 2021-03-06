/*
 * Copyright 2014 Tagbangers, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wallride.core.domain;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.*;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "comment")
@PrimaryKeyJoinColumn
@DynamicInsert
@DynamicUpdate
@Analyzer(definition = "synonyms")
@Indexed
@SuppressWarnings("serial")
public class Comment extends DomainObject<Long> implements Comparable<Comment> {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@IndexedEmbedded(includeEmbeddedObjectId = true)
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@IndexedEmbedded(includeEmbeddedObjectId = true)
	private User author;

	@Column(name = "author_name", length = 200, nullable = false)
	@Field
	private String authorName;

	@Column(nullable = false)
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	@Field
	@FieldBridge(impl=LocalDateTimeBridge.class)
	private LocalDateTime date;

	@Lob
	@Column(nullable = false)
	@Field
	private String content;

	@Column(nullable = false)
	@Field
	private boolean approved;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public int compareTo(Comment comment) {
		return new CompareToBuilder()
				.append(getDate(), comment.getDate())
				.append(getId(), comment.getId())
				.toComparison();
	}
}
