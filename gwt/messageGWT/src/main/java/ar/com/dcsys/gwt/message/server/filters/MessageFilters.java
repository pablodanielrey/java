package ar.com.dcsys.gwt.message.server.filters;

import java.util.ArrayList;
import java.util.List;

public class MessageFilters {

	private List<MessageFilter> filters = new ArrayList<>();
	
	public void addFilter(MessageFilter h) {
		filters.add(h);
	}

	public List<MessageFilter> getFilters() {
		return filters;
	}

	public void setHandlers(List<MessageFilter> filters) {
		this.filters = filters;
	}
	
}
