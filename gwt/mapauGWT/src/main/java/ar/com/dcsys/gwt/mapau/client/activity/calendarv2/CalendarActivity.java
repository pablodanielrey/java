package ar.com.dcsys.gwt.mapau.client.activity.calendarv2;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.dcsys.data.filter.TransferFilterType;
import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.CalendarTypeEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.CalendarTypeEventHandler;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.CreateAppointmentEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.DeleteAppointmentEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.HideViewEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ModifyDragAndDropAppointmentEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.OpenAppointmentEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ShowViewEvent;
import ar.com.dcsys.gwt.mapau.client.events.AppointmentsByFilterFoundEvent;
import ar.com.dcsys.gwt.mapau.client.events.AppointmentsByFilterFoundEventHandler;
import ar.com.dcsys.gwt.mapau.client.events.FindAppointmentsByFiltersEvent;
import ar.com.dcsys.gwt.mapau.client.events.FindAppointmentsByFiltersHandler;
import ar.com.dcsys.gwt.mapau.client.events.RefreshAppointmentsEvent;
import ar.com.dcsys.gwt.mapau.client.filter.DateValue;
import ar.com.dcsys.gwt.mapau.client.manager.AppointmentsManager;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.CalendarView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.CalendarView.CalendarType;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.OpenAppointmentView.Operation;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.calendar.CalendarViewCss;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.calendar.CalendarViewResources;
import ar.com.dcsys.gwt.mapau.shared.data.filter.Filter;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterValue;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SingleSelectionModel;


public class CalendarActivity extends AbstractActivity implements CalendarView.Presenter {

	private ResettableEventBus eventBus;

	private final CalendarView view;
	private final SingleSelectionModel<MapauAppointment> selection;
	private final AppointmentsManager appointmentsManager;

	private Map<Operation,Receiver<List<MapauAppointment>>> handlers;
	
	
	
	public CalendarActivity(AppointmentsManager ap, CalendarView view) {
		this.view = view;
		this.appointmentsManager = ap;
		selection = new SingleSelectionModel<MapauAppointment>();
	}
	
	
	private final Receiver<List<MapauAppointment>> refreshReceiver = new Receiver<List<MapauAppointment>>() {
		@Override
		public void onSuccess(List<MapauAppointment> arg0) {
			eventBus.fireEvent(new RefreshAppointmentsEvent());
		};
		@Override
		public void onFailure(Throwable error) {
			showMessage(error.getMessage());
		};
	};
	
	
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		
		selection.clear();
		
		this.eventBus = new ResettableEventBus(eventBus);
		
		this.handlers = new HashMap<Operation,Receiver<List<MapauAppointment>>>();
		handlers.put(Operation.DELETE, refreshReceiver);
		handlers.put(Operation.EDIT, refreshReceiver);
		handlers.put(Operation.REPEAT, refreshReceiver);
		handlers.put(Operation.ASSIGN, refreshReceiver);
		
		
		this.eventBus.addHandler(CalendarTypeEvent.TYPE, new CalendarTypeEventHandler() {

			@Override
			public void onCalendarType(CalendarTypeEvent event) {
				if (view == null) {
					return;
				}
				CalendarType type = event.getCalendarType();
				if (type == null) {
					return;
				}
				view.setType(type);
			}
			
		});
		
		this.eventBus.addHandler(AppointmentsByFilterFoundEvent.TYPE, new AppointmentsByFilterFoundEventHandler() {
			@Override
			public void onAppointmentsFound(AppointmentsByFilterFoundEvent event) {
				if (view == null) {
					return;
				}
				selection.clear();
				List<MapauAppointment> apps = event.getAppointments();
				setAppointmnets(apps);
			}
		});
		
		// cambio la fecha del calendario
		this.eventBus.addHandler(FindAppointmentsByFiltersEvent.TYPE, new FindAppointmentsByFiltersHandler() {
			@Override
			public void onFindAppointmentsByFilters(FindAppointmentsByFiltersEvent event) {
				List<FilterValue<?>> filters = event.getFilters();
				if (filters == null || filters.size() <= 0 || view == null) {
					return;
				}
				for (FilterValue<?> fv : filters) {
					Filter<?> f = fv.getSelected();
					if (TransferFilterType.DATE.equals(f.getType())) {
						DateValue dateValue = (DateValue)f.getValue();
						Date start = dateValue.getStart();
						view.setDate(start);
						break;
					}
				}
				
			}
		});
		
		
		view.setPresenter(this);
		view.setSelectionModel(selection);
		
		CalendarViewCss style = CalendarViewResources.INSTANCE.style(); 
		style.ensureInjected();	

		panel.setWidget(view);
	}
	
	@Override
	public void onStop() {
		eventBus.removeHandlers();
		view.clear();
		view.setPresenter(null);
	}	
	
	
	@Override
	public void open() {
		MapauAppointment mapp = selection.getSelectedObject();
		if (mapp == null) {
			showMessage("Debe seleccionar alguna reserva");
			return;
		}
		eventBus.fireEvent(new OpenAppointmentEvent(mapp,handlers));
	}
	
	
	@Override
	public void create(Date date) {
		selection.clear();
		eventBus.fireEvent(new CreateAppointmentEvent(date,refreshReceiver));
	}	
	
	
	
	/**
	 * PARCHE HORRIBLE!!! por restricciones del calendario.
	 * el drag&drop modifica el original, y antes de modificarlo lo clona.
	 * o sea que el original es modificado y el clonado tiene los datos "originales"
	 * 
	 * @param original
	 * @param cloned
	 */
	@Override
	public void modify(final MapauAppointment original, MapauAppointment cloned) {
		selection.clear();
		
		////////// PROCESAMIENTO PARA EL PARCHE ASQUEROSO DEL CALENDAR ///////////////////////
		
		// estas son las fechas MODIFICADAS por el calendario
		final Date start = original.getStart();
		final Date end = original.getEnd();
		
		// restauro las fechas originales antes de la modificacion realizada por el calendario en el drop
		original.setStart(new Date(cloned.getStart().getTime()));
		original.setEnd(new Date(cloned.getEnd().getTime()));
		
		
		/////////////////////////////////////////////////////////////////////////////////

//		appointmentsManager.replace(original);
		eventBus.fireEvent(new ModifyDragAndDropAppointmentEvent(start, end, original, refreshReceiver));
	}

	//////////////////////////////  eliminación ///////////////////////////////////
	
	
	@Override
	public void delete() {
		final MapauAppointment mapp = selection.getSelectedObject();
		if (mapp == null) {
			showMessage("Debe seleccionar alguna reserva");
			return;
		}

		selection.clear();
		eventBus.fireEvent(new DeleteAppointmentEvent(mapp, refreshReceiver));
	}	
	
	
	////////////////////////////////////////////////////////////////
	
	
	private void setAppointmnets(List<MapauAppointment> mapps) {
		view.setAppointments(mapps);
	}
	
	
	private void showMessage(String message) {
		eventBus.fireEvent(new MessageDialogEvent(message));
	}
	

	@Override
	public void commit() {
		selection.clear();
		appointmentsManager.commit(new Receiver<Void>() {
			@Override
			public void onSuccess(Void arg0) {
				eventBus.fireEvent(new RefreshAppointmentsEvent());
			}
			@Override
			public void onFailure(Throwable error) {
				showMessage(error.getMessage());
			}
		});
	}
		
	@Override
	public void cancel() {
		selection.clear();
		appointmentsManager.cancel();
		eventBus.fireEvent(new RefreshAppointmentsEvent(true));
	}


	@Override
	public void showView(String view) {
		eventBus.fireEvent(new ShowViewEvent(view));
	}


	@Override
	public void hideView(String view) {
		eventBus.fireEvent(new HideViewEvent(view));
	}	
	
}
