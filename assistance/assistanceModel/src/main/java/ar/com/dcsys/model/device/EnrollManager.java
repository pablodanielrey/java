package ar.com.dcsys.model.device;

import ar.com.dcsys.security.Fingerprint;

public interface EnrollManager {
	
	public void onMessage(EnrollAction action);
	public void onSuccess(String fingerprint);
	
}
