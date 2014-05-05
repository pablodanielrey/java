package ar.com.dcsys.gwt.messages.server.cdi;

import java.util.List;

import javax.enterprise.event.Event;
import javax.inject.Inject;

public class HandlerDetection<T> {
	
	@Inject
	private Event<HandlersContainer<T>> discover;
	
	public List<T> discover() {
		HandlersContainer<T> hc = new HandlersContainer<T>();
		discover.fire(hc);
		return hc.getHandlers();
	}
	
}
