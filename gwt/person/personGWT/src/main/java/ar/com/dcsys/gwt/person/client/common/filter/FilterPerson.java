package ar.com.dcsys.gwt.person.client.common.filter;

import ar.com.dcsys.data.person.Person;

public interface FilterPerson {
	public boolean checkFilter(Person p, String filter);
}
