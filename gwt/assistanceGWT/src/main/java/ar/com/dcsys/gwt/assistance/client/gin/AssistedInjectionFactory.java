package ar.com.dcsys.gwt.assistance.client.gin;

import ar.com.dcsys.gwt.assistance.client.activity.auth.PinAuthDataActivity;
import ar.com.dcsys.gwt.assistance.client.activity.periods.DailyPeriodsActivity;
import ar.com.dcsys.gwt.assistance.client.place.DailyPeriodsPlace;
import ar.com.dcsys.gwt.assistance.client.place.PinAuthDataPlace;



public interface AssistedInjectionFactory {
	public PinAuthDataActivity pinAuthDataActivity(PinAuthDataPlace place);
	public DailyPeriodsActivity dailyPeriodsActivity(DailyPeriodsPlace place);
}
