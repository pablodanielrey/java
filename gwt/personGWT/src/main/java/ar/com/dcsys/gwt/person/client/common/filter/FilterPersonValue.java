package ar.com.dcsys.gwt.person.client.common.filter;

import ar.com.dcsys.gwt.person.shared.PersonValueProxy;

public interface FilterPersonValue {
	public boolean checkFilter(PersonValueProxy p, String filter);
}
