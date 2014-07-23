package ar.com.dcsys.gwt.ws.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;
import javax.websocket.Session;

@Singleton
public class SessionRegister {

	private final Map<String,Session> sessions = new ConcurrentHashMap<>();
	
	public void registerSession(Session s) {
		sessions.put(s.getId(), s);
	}
	
	public void unregisterSession(Session s) {
		sessions.remove(s.getId());
	}
	
	public Session get(String id) {
		return sessions.get(id);
	}
	
	public List<String> getIds() {
		return new ArrayList<String>(sessions.keySet());
	}
 	
}
