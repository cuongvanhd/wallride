package org.wallride.web.controller.admin.user;

import org.hibernate.validator.constraints.Email;
import org.springframework.beans.BeanUtils;
import org.wallride.core.domain.PersonalName;
import org.wallride.core.domain.User;
import org.wallride.core.service.UserUpdateRequest;
import org.wallride.web.support.DomainObjectEditForm;

import javax.persistence.Lob;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@SuppressWarnings("serial")
public class UserEditForm extends DomainObjectEditForm {

	@NotNull
	private Long id;

	@Valid
	private Name name = new Name();

	@NotNull
	@Email
	private String email;

	@Lob
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserUpdateRequest buildUserUpdateRequest() {
		UserUpdateRequest.Builder builder = new UserUpdateRequest.Builder();
		return builder
				.id(id)
				.name(new PersonalName(name.firstName, name.lastName))
				.email(email)
				.description(description)
				.build();
	}

	public static UserEditForm fromDomainObject(User user) {
		UserEditForm form = new UserEditForm();
		BeanUtils.copyProperties(user, form);
		Name name = new Name();
		name.setFirstName(user.getName().getFirstName());
		name.setLastName(user.getName().getLastName());
		form.setName(name);
		return form;
	}

	public static class Name implements Serializable {

		@NotNull
		private String firstName;

		@NotNull
		private String lastName;

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
	}
}