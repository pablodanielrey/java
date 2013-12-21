package ar.com.dcsys.gwt.message.server.handlers;

import java.util.List;

import javax.enterprise.event.Event;
import javax.inject.Inject;


public class MessageHandlersDetection {

	@Inject
	private Event<MessageHandlers> discover;
	
	public List<MethodHandler> detectMethodHandlers() {
		MessageHandlers mh = new MessageHandlers();
		discover.fire(mh);
		return mh.getHandlers();
	}
	
}
