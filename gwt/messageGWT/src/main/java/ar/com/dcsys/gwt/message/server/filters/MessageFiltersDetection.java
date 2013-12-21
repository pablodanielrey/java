package ar.com.dcsys.gwt.message.server.filters;

import java.util.List;

import javax.enterprise.event.Event;
import javax.inject.Inject;


public class MessageFiltersDetection {

	@Inject
	private Event<MessageFilters> discover;
	
	public List<MessageFilter> detectMessageFilters() {
		MessageFilters mh = new MessageFilters();
		discover.fire(mh);
		return mh.getFilters();
	}
	
}
