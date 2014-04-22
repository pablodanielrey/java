package ar.com.dcsys.gwt.mapau.client.ui.calendarv2;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.CalendarView.CalendarType;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterType;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterValue;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public interface ToolBarView extends IsWidget {

	public void setSelectionModel(MultiSelectionModel<FilterValue<?>> selection);
	public void setFilters(List<FilterType<?>> filters);
	
	public void setCalendarTypeSelectionModel(SingleSelectionModel<CalendarType> selection);
	public void setCalendarTypes(List<CalendarType> types);
	

	// TODO: esta solo para parchear y generar el filtro al principio!!!.
	public void setFilterDate(Date start, Date end);
	
	public void setPresenter(Presenter p);
	public void clear();
	
	public interface Presenter {
		public void showView(String view);
		public void hideView(String view);
		public void update();
	}
	
}
