package ar.com.dcsys.gwt.assistance.client.ui.period.daily;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface DailyPeriodsViewResources extends ClientBundle {

	public static final DailyPeriodsViewResources INSTANCE = GWT.create(DailyPeriodsViewResources.class);
	
	@Source("DailyPeriods.css")
	public DailyPeriodsViewCss style();
}
