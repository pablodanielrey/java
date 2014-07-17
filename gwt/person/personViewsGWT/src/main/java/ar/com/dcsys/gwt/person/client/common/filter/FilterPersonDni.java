package ar.com.dcsys.gwt.person.client.common.filter;

import ar.com.dcsys.data.person.Person;

public class FilterPersonDni implements FilterPerson{

	@Override
	public boolean checkFilter(Person p, String filter) {
		if (p.getDni() == null) {
			return false;
		} else {
			return p.getDni().matches("^.*" + filter + ".*$");
		}
	}

}
