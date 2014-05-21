package ar.com.dcsys.gwt.assistance.client.gin;


import ar.com.dcsys.gwt.assistance.shared.PeriodsManagerTransfer;

import com.google.gwt.core.client.GWT;
import com.google.inject.Provider;

public class PeriodsManagerProvider implements Provider<PeriodsManagerTransfer> {
	
	private static PeriodsManagerTransfer pm = null;
	
	@Override
	public PeriodsManagerTransfer get() {
		if (pm == null) {
			pm = GWT.create(PeriodsManagerTransfer.class);			
		}
		return pm;
	}

}
