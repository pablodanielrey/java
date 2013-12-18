package ar.com.dcsys.data.auth;

import java.util.ArrayList;
import java.util.List;

public class AuthHandlers {

	private final List<AuthHandler> handlers = new ArrayList<>();
	
	public void addHandler(AuthHandler h) {
		handlers.add(h);
	}

	public List<AuthHandler> getHandlers() {
		return handlers;
	}
	
}
