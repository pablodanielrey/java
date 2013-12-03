package ar.com.dcsys.gwt.person.client.common.filter;

import ar.com.dcsys.gwt.person.shared.PersonValueProxy;

public class FilterPersonValueName implements FilterPersonValue{

	@Override
	public boolean checkFilter(PersonValueProxy p, String filter) {
		boolean match = false;
		String toCheck = p.getName();
		if (toCheck != null) {
			match = toCheck.toLowerCase().matches("^.*" + filter + ".*$");
		}
		toCheck = p.getLastName();
		if ((!match) && (toCheck != null)) {
			match = toCheck.toLowerCase().matches("^.*" + filter + ".*$");
		}
		return match;
	}

}
