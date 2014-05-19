package ar.com.dcsys.gwt.auth.server;

import javax.inject.Inject;

import ar.com.dcsys.exceptions.AuthenticationException;
import ar.com.dcsys.gwt.auth.shared.AuthManagerTransfer;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.model.auth.AuthManager;

public class AuthManagerTransferBean implements AuthManagerTransfer {

	private final AuthManager authManager;
	
	@Inject
	public AuthManagerTransferBean(AuthManager authManager) {
		this.authManager = authManager;
	}
	
	@Override
	public void isAuthenticated(Receiver<Boolean> rec) {
		try {
			boolean t = authManager.isAuthenticated();
			rec.onSuccess(t);
			
		} catch (AuthenticationException e) {
			rec.onError(e.getMessage());
		}
	}

	@Override
	public void hasPermission(String perm, Receiver<Boolean> rec) {
		try {
			boolean permission = authManager.hasPermission(perm);
			rec.onSuccess(permission);
			
		} catch (AuthenticationException e) {
			rec.onError(e.getMessage());
		}
	}

}
