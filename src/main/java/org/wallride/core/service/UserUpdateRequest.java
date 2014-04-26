package org.wallride.core.service;

import org.wallride.core.domain.PersonalName;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserUpdateRequest implements Serializable {

	private Long id;
	private PersonalName name;
	private String email;
	private String description;

	public Long getId() {
		return id;
	}

	public PersonalName getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getDescription() {
		return description;
	}

	public static class Builder  {

		private Long id;
		private String loginId;
		private String loginPassword;
		private PersonalName name;
		private String email;
		private String description;

		public Builder() {
		}

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder loginId(String loginId) {
			this.loginId = loginId;
			return this;
		}

		public Builder loginPassword(String loginPassword) {
			this.loginPassword = loginPassword;
			return this;
		}

		public Builder name(PersonalName name) {
			this.name = name;
			return this;
		}

		public Builder email(String email) {
			this.email = email;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public UserUpdateRequest build() {
			UserUpdateRequest request = new UserUpdateRequest();
			request.id = id;
			request.name = name;
			request.email = email;
			request.description = description;
			return request;
		}
	}
}
