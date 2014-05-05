package ar.com.dcsys.gwt.mapau.client.activity.calendarv2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.CalendarTypeEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.HideViewEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ShowViewEvent;
import ar.com.dcsys.gwt.mapau.client.events.FindAppointmentsByFiltersEvent;
import ar.com.dcsys.gwt.mapau.client.manager.FilterActivityUtils;
import ar.com.dcsys.gwt.mapau.client.manager.MapauManager;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.CalendarView.CalendarType;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.ToolBarView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.toolbar.ToolBarViewCss;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.toolbar.ToolBarViewResources;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterType;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterValue;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;


public class ToolBarActivity extends AbstractActivity implements ToolBarView.Presenter {

	private final ToolBarView view;
	private final MultiSelectionModel<FilterValue<?>> selection;
	private final MapauManager rf;
	private final SingleSelectionModel<CalendarType> calendarTypeSelection;
	private final FilterActivityUtils utils;
	
	private ResettableEventBus eventBus;
	
	public ToolBarActivity(MapauManager rf,
						   FilterActivityUtils utils,
						   ToolBarView view, 
						   MultiSelectionModel<FilterValue<?>> selection) {
		this.view = view;
		this.selection = selection;		
		this.rf = rf;
		this.utils = utils;
		
		calendarTypeSelection = new SingleSelectionModel<CalendarType>();
		calendarTypeSelection.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (eventBus == null) {
					return;
				}
				CalendarType type = calendarTypeSelection.getSelectedObject();
				if (type == null) {
					return;
				}
				
				eventBus.fireEvent(new CalendarTypeEvent(type));
			}
		});
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = new ResettableEventBus(eventBus);
		
		view.setPresenter(this);
		view.setSelectionModel(selection);
		view.setCalendarTypeSelectionModel(calendarTypeSelection);
		
		ToolBarViewCss style = ToolBarViewResources.INSTANCE.style(); 
		style.ensureInjected();			
		
		panel.setWidget(view);
		
		findCalendarTypes();
		findFilters();
	}
	
	@Override
	public void onStop() {
		this.eventBus = null;
		view.clear();
		view.setPresenter(null);
		view.setSelectionModel(null);
		view.setCalendarTypeSelectionModel(null);
	}
	
	private void findCalendarTypes() {
		calendarTypeSelection.setSelected(CalendarType.WEEKLY, true);
		view.setCalendarTypes(Arrays.asList(CalendarType.values()));
	}
	

	private void findFilters() {
		utils.findFilters(new Receiver<List<FilterType<?>>>() {
			@Override
			public void onSuccess(List<FilterType<?>> filters) {
				view.setFilters(filters);

				
				// selecciono la fecha actual para el inicio de la visualización del calendario
				
				Date start= new Date();
				start.setHours(0);
				start.setMinutes(0);
				start.setSeconds(0);
				
				Long week = 1000l * 60l * 60l * 24l * 7l;
				Date end = new Date(start.getTime() + week);
				end.setHours(23);
				end.setMinutes(59);
				end.setSeconds(59);					
				
				view.setFilterDate(start, end);
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
	public void update() {
		Set<FilterValue<?>> filters = selection.getSelectedSet();
		if (filters == null) {
			showMessage("filters == null");
			return;
		}
		eventBus.fireEvent(new FindAppointmentsByFiltersEvent(new ArrayList<FilterValue<?>>(filters)));
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
