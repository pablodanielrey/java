package ar.com.dcsys.gwt.auth.shared;

import ar.com.dcsys.gwt.manager.shared.Receiver;

public interface AuthManagerTransfer {

	public void isAuthenticated(Receiver<Boolean> rec);
	public void hasPermission(String perm, Receiver<Boolean> rec);
	
}
