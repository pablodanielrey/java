package ar.com.dcsys.gwt.mapau.client.activity.calendarv2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.HideViewEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.HideViewEventHandler;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ShowViewEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ShowViewEventHandler;
import ar.com.dcsys.gwt.mapau.client.events.AppointmentsByFilterFoundEvent;
import ar.com.dcsys.gwt.mapau.client.events.FindAppointmentsByFiltersEvent;
import ar.com.dcsys.gwt.mapau.client.events.FindAppointmentsByFiltersHandler;
import ar.com.dcsys.gwt.mapau.client.events.RefreshAppointmentsEvent;
import ar.com.dcsys.gwt.mapau.client.events.RefreshAppointmentsEventHandler;
import ar.com.dcsys.gwt.mapau.client.manager.AppointmentsManager;
import ar.com.dcsys.gwt.mapau.client.manager.ClassRoomsManager;
import ar.com.dcsys.gwt.mapau.client.manager.FilterActivityUtils;
import ar.com.dcsys.gwt.mapau.client.manager.MapauManager;
import ar.com.dcsys.gwt.mapau.client.manager.SilegManager;
import ar.com.dcsys.gwt.mapau.client.place.MainCalendarPlace;
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
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.mainCalendar.MainCalendarViewCss;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.mainCalendar.MainCalendarViewResources;
import ar.com.dcsys.gwt.mapau.client.ui.common.UserValidationMessageView;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterValue;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;


public class MainCalendarActivity extends AbstractActivity  implements MainCalendarView.Presenter, HideViewEventHandler, ShowViewEventHandler, FindAppointmentsByFiltersHandler, RefreshAppointmentsEventHandler {

	private final CreateModifyAppointmentsView createModifyAppointmentsView;
	private final FiltersView filtersView;
	private final ToolBarView toolbarView;
	private final DeleteAppointmentsView deleteAppointmentsView;
	private final CalendarView calendarView;
	private final MainCalendarView view;
	private final OpenAppointmentView openAppointmentView;
	private final GenerateRepetitionView repeatAppointmentView;
	private final AssignClassroomView assignClassroomView;
	private final ThisOrRelatedView thisOrRelatedView;
	private final UserValidationMessageView viewUserValidation;
	
	private final CreateAppointmentActivity createAppointmentActivity;
	private final ModifyAppointmentActivity modifyAppointmentActivity;
	private final FilterActivity filterActivity;
	private final ToolBarActivity toolbarActivity;
	private final DeleteAppointmentsActivity deleteAppointmentsActivity;
	private final CalendarActivity calendarActivity;
	private final OpenAppointmentActivity openAppointmentActivity;
	private final CreateAppointmentRepetitionActivity createAppointmentRepetition;
	private final AssignClassroomActivity assignClassroomActivity;
	private final ThisOrRelatedActivity thisOrRelatedActivity;
	
	
	private final GenerateRepetitionActivity repeatAppointmentActivity;
	
	private final MapauManager rf;
	private final SilegManager sf;
	private final ClassRoomsManager cf;
	private final AppointmentsManager appointmentsManager;
	private final FilterActivityUtils utils;

	private ResettableEventBus eventBus;
	private final MultiSelectionModel<FilterValue<?>> selectedFilters;
	
	@Override
	public void hideView(HideViewEvent event) {
		if (view != null) {
			view.hideView(event.getView());
		}
	}
	
	@Override
	public void showView(ShowViewEvent event) {
		if (view != null) {
			view.showView(event.getView());
		}
	}
	
	
	@Inject
	public MainCalendarActivity(MapauManager rf, 
								SilegManager sf,
								ClassRoomsManager cf,
								AppointmentsManager appointmentsManager, 
								MainCalendarView view, 
								CreateModifyAppointmentsView cmav, 
								FiltersView filtersView, 
								ToolBarView toolbarView, 
								DeleteAppointmentsView deleteAppointmentsView,
								OpenAppointmentView openAppointmentView,
								GenerateRepetitionView repeatAppointmentView,
								CalendarView calendarView,
								AssignClassroomView assignClassroomView,
								ThisOrRelatedView thisOrRelatedView,
								UserValidationMessageView viewUserValidation,
								FilterActivityUtils utils,
								@Assisted MainCalendarPlace place) {
		
		this.rf = rf;
		this.sf = sf;
		this.cf = cf;
		this.appointmentsManager = appointmentsManager;
		this.utils = utils;
		
		selectedFilters = new MultiSelectionModel<FilterValue<?>>();
		
		this.view = view;
		this.createModifyAppointmentsView = cmav;
		this.filtersView = filtersView;
		this.toolbarView = toolbarView;
		this.calendarView = calendarView;
		this.deleteAppointmentsView = deleteAppointmentsView;
		this.openAppointmentView = openAppointmentView;
		this.repeatAppointmentView = repeatAppointmentView;
		this.assignClassroomView = assignClassroomView;
		this.thisOrRelatedView = thisOrRelatedView;
		this.viewUserValidation = viewUserValidation;
		
		modifyAppointmentActivity = new ModifyAppointmentActivity(appointmentsManager,rf,sf,cf,createModifyAppointmentsView);
		createAppointmentActivity = new CreateAppointmentActivity(appointmentsManager,rf,sf,cf,createModifyAppointmentsView);
		deleteAppointmentsActivity = new DeleteAppointmentsActivity(appointmentsManager,deleteAppointmentsView);
		openAppointmentActivity = new OpenAppointmentActivity(openAppointmentView);
		createAppointmentRepetition = new CreateAppointmentRepetitionActivity(appointmentsManager);
		repeatAppointmentActivity = new GenerateRepetitionActivity(repeatAppointmentView);
		calendarActivity = new CalendarActivity(appointmentsManager,calendarView);
		filterActivity = new FilterActivity(rf,utils,filtersView,selectedFilters);
		toolbarActivity = new ToolBarActivity(rf,utils,toolbarView,selectedFilters);
		assignClassroomActivity = new AssignClassroomActivity(rf, appointmentsManager, assignClassroomView,viewUserValidation);
		thisOrRelatedActivity = new ThisOrRelatedActivity(thisOrRelatedView);
		
	}
	
	@Override
	public void onRefreshAppointmnets(RefreshAppointmentsEvent event) {
		Set<FilterValue<?>> filters = selectedFilters.getSelectedSet();
		List<FilterValue<?>> lfilters = new ArrayList<FilterValue<?>>(filters);
		findByFilters(lfilters);
	}

	private void findByFilters(List<FilterValue<?>> filters) {
		appointmentsManager.findByFilters(filters, new Receiver<List<MapauAppointment>>() {
			@Override
			public void onSuccess(List<MapauAppointment> apps) {
				eventBus.fireEvent(new AppointmentsByFilterFoundEvent(apps));
			}
			@Override
			public void onFailure(Throwable error) {
				showMessage(error.getMessage());
			}
		});		
	}
	
	
	private void showMessage(String message) {
		eventBus.fireEvent(new MessageDialogEvent(message));
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = new ResettableEventBus(eventBus);
		
		AcceptsOneWidget c;
		
		c = view.getCreateModifyAppointmentContainer();
		modifyAppointmentActivity.start(c,eventBus);
		createAppointmentActivity.start(c,eventBus);
		
		c = view.getFiltersContainer();
		filterActivity.start(c, eventBus);
		
		c = view.getToolBarContainer();
		toolbarActivity.start(c, eventBus);
		
		c = view.getDeleteAppointmentsContainer();
		deleteAppointmentsActivity.start(c, eventBus);
		
		c = view.getOpenAppointmentsContainer();
		openAppointmentActivity.start(c, eventBus);
		
		c = view.getRepeatAppointmentContainer();
		repeatAppointmentActivity.start(c, eventBus);
		
		createAppointmentRepetition.start(null, eventBus);
		
		c = view.getAssignClassroomContainer();
		assignClassroomActivity.start(c, eventBus);
		
		c = view.getCalendarContainer();
		calendarActivity.start(c, eventBus);
		
		c = view.getThisOrRelatedContainer();
		thisOrRelatedActivity.start(c, eventBus);
		
		MainCalendarViewCss style = MainCalendarViewResources.INSTANCE.style(); 
		style.ensureInjected();	
		
		this.eventBus.addHandler(HideViewEvent.TYPE, this);
		this.eventBus.addHandler(ShowViewEvent.TYPE, this);
		this.eventBus.addHandler(FindAppointmentsByFiltersEvent.TYPE,this);
		this.eventBus.addHandler(RefreshAppointmentsEvent.TYPE, this);
		
		panel.setWidget(view);
	}

	@Override
	public void onStop() {
		eventBus.removeHandlers();
		
		modifyAppointmentActivity.onStop();
		createAppointmentActivity.onStop();
		filterActivity.onStop();
		toolbarActivity.onStop();
		deleteAppointmentsActivity.onStop();
		openAppointmentActivity.onStop();
		repeatAppointmentActivity.onStop();
		createAppointmentRepetition.onStop();
		calendarActivity.onStop();
		thisOrRelatedActivity.onStop();
	}

	@Override
	public void onFindAppointmentsByFilters(FindAppointmentsByFiltersEvent event) {
		findByFilters(event.getFilters());
	}
	
}
