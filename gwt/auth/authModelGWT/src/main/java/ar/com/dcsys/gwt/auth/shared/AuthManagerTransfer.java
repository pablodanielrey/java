package ar.com.dcsys.gwt.auth.shared;

import ar.com.dcsys.gwt.manager.shared.Manager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.pr.ClientManager;

@ClientManager
public interface AuthManagerTransfer extends Manager {

	public void isAuthenticated(Receiver<Boolean> rec);
	public void hasPermission(String perm, Receiver<Boolean> rec);
	
}
