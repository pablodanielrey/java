package ar.com.dcsys.gwt.mapau.client.ui.calendarv2;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SingleSelectionModel;

public interface CalendarView extends IsWidget {

	public void setPresenter(Presenter p);
	public void clear();

	public void setDate(Date date);
	public void setType(CalendarType type);
	public void setAppointments(List<MapauAppointment> appointments);
	
	public void disabledDragAndDrop(boolean v);
	
	public void setSelectionModel(SingleSelectionModel<MapauAppointment> selection); 
	
	
	public enum CalendarType {
		DAILY("Diaria"), WEEKLY("Semanal"), MONTHLY("Mensual");
		
		private final String description;
		
		private CalendarType(String description) {
			this.description = description;
		}
		
		public String getDescription() {
			return description;
		}
	}
	
	
	public interface Presenter {
		public void showView(String view);
		public void hideView(String view);
		
		public void open();
		public void create(Date date);
		public void modify(MapauAppointment original, MapauAppointment cloned);
		public void delete();
		
		public void commit();
		public void cancel();
	}
	
}
