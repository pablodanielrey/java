package ar.com.dcsys.gwt.mapau.client.gin;

import ar.com.dcsys.gwt.mapau.client.manager.AppointmentsManager;
import ar.com.dcsys.gwt.mapau.client.manager.AppointmentsManagerBean;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.AssignClassroomView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.CalendarView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.CreateModifyAppointmentsView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.DeleteAppointmentsView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.FiltersView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.GenerateRepetitionView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.MainCalendarView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.OpenAppointmentView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.ThisOrRelatedView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.ToolBarView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.assign.AssignClassroom;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.delete.DeleteAppointments;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.open.OpenAppointment;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.related.ThisOrRelated;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.repeat.GenerateRepetition;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.update.CreateModifyAppointments;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.calendar.Calendar;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.filter.Filters;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.mainCalendar.MainCalendar;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.toolbar.ToolBar;
import ar.com.dcsys.gwt.mapau.client.ui.common.UserValidationMessage;
import ar.com.dcsys.gwt.mapau.client.ui.common.UserValidationMessageView;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.Singleton;

public class MapauGwtGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		
		bind(UserValidationMessageView.class).to(UserValidationMessage.class).in(Singleton.class);
		
		bind(MainCalendarView.class).to(MainCalendar.class).in(Singleton.class);
		bind(AssignClassroomView.class).to(AssignClassroom.class).in(Singleton.class);
		bind(CalendarView.class).to(Calendar.class).in(Singleton.class);
		bind(CreateModifyAppointmentsView.class).to(CreateModifyAppointments.class).in(Singleton.class);
		bind(DeleteAppointmentsView.class).to(DeleteAppointments.class).in(Singleton.class);
		bind(FiltersView.class).to(Filters.class).in(Singleton.class);
		bind(GenerateRepetitionView.class).to(GenerateRepetition.class).in(Singleton.class);
		bind(OpenAppointmentView.class).to(OpenAppointment.class).in(Singleton.class);
		bind(ThisOrRelatedView.class).to(ThisOrRelated.class).in(Singleton.class);
		bind(ToolBarView.class).to(ToolBar.class).in(Singleton.class);
		
		bind(AppointmentsManager.class).to(AppointmentsManagerBean.class).in(Singleton.class);
		
		GinFactoryModuleBuilder builder = new GinFactoryModuleBuilder();
		install(builder.build(MapauAssistedInjectionFactory.class));
		
	}
	
}
