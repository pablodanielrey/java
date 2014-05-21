package ar.com.dcsys.gwt.assistance.client.gin;


import ar.com.dcsys.gwt.assistance.shared.JustificationsManagerTransfer;

import com.google.gwt.core.client.GWT;
import com.google.inject.Provider;

public class JustificationsManagerProvider implements Provider<JustificationsManagerTransfer> {

	private static JustificationsManagerTransfer jm  = null; 
	
	@Override
	public JustificationsManagerTransfer get() {
		if (jm == null) {
			jm = GWT.create(JustificationsManagerTransfer.class);
		}
		return jm;
	}
}
