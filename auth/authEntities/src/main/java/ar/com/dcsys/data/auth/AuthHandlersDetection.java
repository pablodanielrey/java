package ar.com.dcsys.data.auth;

import java.util.List;

import javax.enterprise.event.Event;
import javax.inject.Inject;

public class AuthHandlersDetection {

	@Inject
	private Event<AuthHandlers> event;
	
	public List<AuthHandler> detectAuthHandlers() {
		AuthHandlers hs = new AuthHandlers();
		event.fire(hs);
		return hs.getHandlers();
	}
	
}
