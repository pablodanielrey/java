package ar.com.dcsys.gwt.person.client.common.filter;

import ar.com.dcsys.data.person.Person;

public class FilterPersonName implements FilterPerson {

	@Override
	public boolean checkFilter(Person p, String filter) {
		boolean match = false;
		String toCheck = p.getName();
		if (toCheck != null) {
			match = toCheck.toLowerCase().matches("^.*" + filter.toLowerCase() + ".*$");
		}
		toCheck = p.getLastName();
		if ((!match) && (toCheck != null)) {
			match = toCheck.toLowerCase().matches("^.*" + filter.toLowerCase() + ".*$");
		}
		return match;
	}

	
}
