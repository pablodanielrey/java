package ar.com.dcsys.gwt.person.client.common.filter;

import ar.com.dcsys.data.group.Group;

public interface FilterGroup {
	public boolean checkFilter(Group g, String filter);
}
