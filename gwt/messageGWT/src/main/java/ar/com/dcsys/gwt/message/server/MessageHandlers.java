package ar.com.dcsys.gwt.message.server;

import java.util.ArrayList;
import java.util.List;

public class MessageHandlers {

	private List<MethodHandler> handlers = new ArrayList<MethodHandler>();
	
	public void addHandler(MethodHandler h) {
		handlers.add(h);
	}

	public List<MethodHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(List<MethodHandler> handlers) {
		this.handlers = handlers;
	}
	
}
