package ar.com.dcsys.gwt.assistance.client.gin;

import ar.com.dcsys.gwt.assistance.client.activity.auth.PinAuthDataActivity;
import ar.com.dcsys.gwt.assistance.client.place.PinAuthDataPlace;



public interface AssistedInjectionFactory {
	public PinAuthDataActivity pinAuthDataActivity(PinAuthDataPlace place);
}
