package ar.com.dcsys.gwt.mapau.client.ui.calendarv2;

import java.util.List;

import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;

import com.google.gwt.user.client.ui.IsWidget;

public interface OpenAppointmentView extends IsWidget {
	
	public void setPresenter(Presenter p);
	public void clear();
	
	public void setAppointment(MapauAppointment app);
	public void setOperations(List<Operation> operations);
	
	public enum Operation {
		DELETE,	EDIT, ASSIGN, REPEAT
	} 
	
	public interface Presenter {
		public void startOperation(Operation op);
	}
}
