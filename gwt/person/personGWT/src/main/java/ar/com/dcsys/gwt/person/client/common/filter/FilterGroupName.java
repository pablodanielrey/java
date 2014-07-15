package ar.com.dcsys.gwt.person.client.common.filter;

import ar.com.dcsys.data.group.Group;

public class FilterGroupName implements FilterGroup {

	@Override
	public boolean checkFilter(Group g, String filter) {
		boolean match = false;
		String toCheck = g.getName();	
		if (toCheck != null) {
			match = toCheck.toLowerCase().matches("^.*" + filter.toLowerCase() + ".*$");
		}
		return match;
	}

}
