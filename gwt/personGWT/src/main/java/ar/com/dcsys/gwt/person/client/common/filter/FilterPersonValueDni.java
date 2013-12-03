package ar.com.dcsys.gwt.person.client.common.filter;

import ar.com.dcsys.gwt.person.shared.PersonValueProxy;

public class FilterPersonValueDni implements FilterPersonValue{

	@Override
	public boolean checkFilter(PersonValueProxy p, String filter) {
		if (p.getDni() == null) {
			return false;
		} else {
			return p.getDni().matches("^.*" + filter + ".*$");
		}
	}

}
