package ar.com.dcsys.gwt.auth.client.manager;

import ar.com.dcsys.gwt.manager.shared.Receiver;

public interface AuthManager {

	public void login(String username, String password, Receiver<Void> rec);
	public void logout(Receiver<Void> rec);
	public void isAuthenticated(Receiver<Boolean> rec);
	public void hasPermission(String perm, Receiver<Boolean> rec);
	
}
