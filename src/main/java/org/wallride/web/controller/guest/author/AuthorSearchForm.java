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

package org.wallride.web.controller.guest.author;

import org.wallride.core.domain.User;
import org.wallride.core.service.UserSearchRequest;
import org.wallride.web.support.DomainObjectSearchForm;

import java.util.List;

@SuppressWarnings("serial")
public class AuthorSearchForm extends DomainObjectSearchForm {
	
	private String keyword;

	private List<User.Role> roles;

	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<User.Role> getRoles() {
		return roles;
	}

	public void setRoles(List<User.Role> roles) {
		this.roles = roles;
	}

	public UserSearchRequest toUserSearchRequest() {
		return new UserSearchRequest()
				.withKeyword(getKeyword())
				.withRoles(getRoles());
	}
}
