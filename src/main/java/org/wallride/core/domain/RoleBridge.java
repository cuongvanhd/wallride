package org.wallride.core.domain;

import org.hibernate.annotations.common.util.StringHelper;
import org.hibernate.search.bridge.TwoWayStringBridge;
import org.hibernate.search.exception.SearchException;

/**
 * Created by sasaki on 2015/04/21.
 */
public class RoleBridge implements TwoWayStringBridge {

	@Override
	public Object stringToObject(String stringValue) {
		if (StringHelper.isEmpty(stringValue)) {
			return null;
		}
		try {
			return User.Role.valueOf(stringValue);
		}
		catch (Exception e) {
			throw new SearchException( "Unable to parse into role: " + stringValue, e );
		}
	}

	@Override
	public String objectToString(Object object) {
		if (object == null) {
			return null;
		}
		User.Role role = (User.Role) object;
		return role.name();
	}

}
