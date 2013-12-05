package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;
import ar.com.dcsys.gwt.mapau.client.ui.calendar.MyCalendar;
import ar.com.dcsys.gwt.mapau.client.ui.calendar.MyUpdateEvent;
import ar.com.dcsys.gwt.mapau.client.ui.calendar.MyUpdateEventHandler;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.CalendarView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.CalendarView.CalendarType;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.CalendarView.Presenter;

import com.bradrydzewski.gwt.calendar.client.Appointment;
import com.bradrydzewski.gwt.calendar.client.CalendarSettings;
import com.bradrydzewski.gwt.calendar.client.CalendarViews;
import com.bradrydzewski.gwt.calendar.client.event.DeleteEvent;
import com.bradrydzewski.gwt.calendar.client.event.DeleteHandler;
import com.bradrydzewski.gwt.calendar.client.event.TimeBlockClickEvent;
import com.bradrydzewski.gwt.calendar.client.event.TimeBlockClickHandler;
import com.bradrydzewski.gwt.calendar.client.event.UpdateEvent;
import com.bradrydzewski.gwt.calendar.client.event.UpdateHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;

public class Calendar extends Composite implements CalendarView {

	private static CalendarUiBinder uiBinder = GWT.create(CalendarUiBinder.class);

	interface CalendarUiBinder extends UiBinder<Widget, Calendar> {
	}
	
	@UiField FlowPanel calendarContainer;
	@UiField Button save;
	@UiField Button cancel;
	
	private CalendarSettings settings;	
	private MyCalendar calendar;	
	
	private CalendarView.Presenter presenter;
	
	
	@UiField CalendarViewResources res;

	private SingleSelectionModel<MapauAppointment> selectionApp;
	
	private final SelectionHandler<Appointment> selectionHandlerApp = new SelectionHandler<Appointment>() {
		@Override
		public void onSelection(SelectionEvent<Appointment> event) {
			if (selectionApp == null) {
				return;
			}
						
			if (!(event.getSelectedItem() instanceof MapauAppointment)) {
				selectionApp.setSelected(null, true);
				return;
			}
						
			MapauAppointment app = (MapauAppointment) event.getSelectedItem();
			selectionApp.setSelected(app, true);
		}
	};
	
	private final DeleteHandler<Appointment> deleteHandler = new DeleteHandler<Appointment>() {
		@Override
		public void onDelete(DeleteEvent<Appointment> event) {
			event.setCancelled(true);
			if (presenter == null || selectionApp == null) {
				return;
			}

			if (!(event.getTarget() instanceof MapauAppointment)) {
				return;
			}
			MapauAppointment app = (MapauAppointment) event.getTarget();
			if (app == null) {
				return;
			}
			selectionApp.setSelected(app, true);
			presenter.delete();
		}
	};
	
	private final OpenHandler<Appointment> openHandler = new OpenHandler<Appointment>() {
		
		@Override
		public void onOpen(OpenEvent<Appointment> event) {
			if (presenter == null) {
				return;
			}
			presenter.open();
		}
	};
	
	private final TimeBlockClickHandler<Date> timeBlockHandler = new TimeBlockClickHandler<Date>() {
		@Override
		public void onTimeBlockClick(TimeBlockClickEvent<Date> event) {
			
			if (presenter == null) {
				return;
			}
			
			Date date = event.getTarget();
			if (date == null) {
				return;
			}
			presenter.create(date);
		}
	};
	
	private final MyUpdateEventHandler myUpdateHandler = new MyUpdateEventHandler() {

		@Override
		public void onMyUpdate(MyUpdateEvent event) {
			
			if (!(event.getOriginal() instanceof MapauAppointment) || !(event.getCloned() instanceof MapauAppointment) ) {
				return;
			}
			
			final MapauAppointment original = (MapauAppointment)event.getOriginal();
					
			final MapauAppointment cloned = (MapauAppointment)event.getCloned();			
			
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
			
					if (presenter == null || selectionApp == null) {
						return;
					}
					presenter.modify(original, cloned);		
				}
			});
		}
		
	}; 
	
	private final UpdateHandler<Appointment> updateHandler = new UpdateHandler<Appointment>() {
		@Override
		public void onUpdate(UpdateEvent<Appointment> event) {
						
			event.setCancelled(true);
						
		}
	};
	
	public Calendar() {
		initWidget(uiBinder.createAndBindUi(this));
		initializeSettings();		
		createCalendar();
	}
	
	private void initializeSettings() {
		settings = new CalendarSettings();
		settings.setEnableDragDrop(true);
		settings.setScrollToHour(7);
	}
	
	private void createCalendar() {		
		
		calendar = new MyCalendar();
		calendar.setSettings(settings);
		calendar.setDate(new Date());
		calendar.setDays(7);
		calendar.setWidth("100%");
		calendar.setHeight("750px");
		calendarContainer.add(calendar);

		calendar.addSelectionHandler(selectionHandlerApp);
		calendar.addDeleteHandler(deleteHandler);
		calendar.addTimeBlockClickHandler(timeBlockHandler);
		calendar.addUpdateHandler(updateHandler);
		calendar.addOpenHandler(openHandler);
		calendar.addHandler(myUpdateHandler, MyUpdateEvent.TYPE);
	}

	@Override
	public void setPresenter(Presenter p) {
		this.presenter = p;
	}

	@Override
	public void clear() {
		calendar.clearAppointments();
	}

	@Override
	public void setDate(Date date) {
		calendar.setDate(date);
	}

	@Override
	public void setType(CalendarType type) {
		switch(type) {
			case DAILY: calendar.setView(CalendarViews.DAY); calendar.setDays(1); break;
			case WEEKLY: calendar.setView(CalendarViews.DAY); calendar.setDays(7); break;
			case MONTHLY: calendar.setView(CalendarViews.MONTH); break;
		}
	}

	@Override
	public void setAppointments(List<MapauAppointment> appointments) {
		calendar.clearAppointments();
		
		calendar.suspendLayout();
		
		calendar.addAppointments(new ArrayList<Appointment>(appointments));
		
		calendar.resumeLayout();
	}

	@UiHandler("save")
	public void onSave(ClickEvent event) {
		presenter.commit();
	}
	
	@UiHandler("cancel")
	public void onCancel(ClickEvent event) {
		presenter.cancel();
	}
	
	@Override
	public void setSelectionModel(SingleSelectionModel<MapauAppointment> selection) {
		this.selectionApp = selection;
	}
	
	@Override
	public void disabledDragAndDrop(boolean v) {
		settings.setEnableDragDrop(v);
	}


}
