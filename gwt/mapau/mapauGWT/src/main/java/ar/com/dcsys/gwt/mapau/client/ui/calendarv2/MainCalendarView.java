package ar.com.dcsys.gwt.mapau.client.ui.calendarv2;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface MainCalendarView extends IsWidget {

	public AcceptsOneWidget getCreateModifyAppointmentContainer();
	public AcceptsOneWidget getFiltersContainer();
	public AcceptsOneWidget getDeleteAppointmentsContainer();
	public AcceptsOneWidget getOpenAppointmentsContainer();
	public AcceptsOneWidget getRepeatAppointmentContainer();
	public AcceptsOneWidget getCalendarContainer();
	public AcceptsOneWidget getToolBarContainer();
	public AcceptsOneWidget getAssignClassroomContainer();
	public AcceptsOneWidget getThisOrRelatedContainer();
	
	public void setPresenter(Presenter p);
	public void clear();	
	
	public void showView(String view);
	public void hideView(String view);
	
	public interface Presenter {

	}
	
}
