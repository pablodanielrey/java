package ar.com.dcsys.gwt.mapau.client.ui.calendarv2;

import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;

import com.google.gwt.user.client.ui.IsWidget;

public interface DeleteAppointmentsView extends IsWidget {

	public void setAppointment(MapauAppointment a);
	
	public void setPresenter(Presenter p);
	public void clear();
	
	
	public interface Presenter {
		public void onlyOne();
		public void relatedAlso();
		public void cancel();
	}
	
}
