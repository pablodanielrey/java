package ar.com.dcsys.gwt.messages.server.cdi;

import java.util.ArrayList;
import java.util.List;

public class HandlersContainer<T> {

	private final List<T> hs = new ArrayList<>();
	
	public void add(T t) {
		hs.add(t);
	}
	
	public List<T> getHandlers() {
		return hs;
	}
	
}
