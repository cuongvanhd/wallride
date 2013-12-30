package org.wallride.core.web;

import org.wallride.core.domain.DomainObject;

import java.io.Serializable;

public class DomainObjectDeletedModel<ID extends Serializable> implements Serializable {

	private ID id;

	public DomainObjectDeletedModel(DomainObject<ID> object) {
		this.id = object.getId();
	}

	public ID getId() {
		return id;
	}
}
