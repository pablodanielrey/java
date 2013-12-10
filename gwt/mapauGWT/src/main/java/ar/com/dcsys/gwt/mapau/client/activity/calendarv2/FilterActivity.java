package ar.com.dcsys.gwt.mapau.client.activity.calendarv2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.HideViewEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ShowViewEvent;
import ar.com.dcsys.gwt.mapau.client.events.FindAppointmentsByFiltersEvent;
import ar.com.dcsys.gwt.mapau.client.manager.FilterActivityUtils;
import ar.com.dcsys.gwt.mapau.client.manager.MapauManager;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.FiltersView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.filter.FiltersViewCss;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.filter.FiltersViewResources;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterType;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterValue;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.MultiSelectionModel;


public class FilterActivity extends AbstractActivity implements FiltersView.Presenter {

	private final FiltersView view;
	private final MultiSelectionModel<FilterValue<?>> selection;
	private final MapauManager rf;
	private final FilterActivityUtils utils;
	
	private EventBus eventBus;
	
	public FilterActivity(MapauManager rf, FilterActivityUtils utils, FiltersView view, MultiSelectionModel<FilterValue<?>> selection)  {
		this.selection = selection;
		this.view = view;
		this.rf = rf;
		this.utils = utils;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = eventBus;
		
		view.setPresenter(this);
		view.setSelectionModel(selection);
		panel.setWidget(view);
		
		FiltersViewCss style = FiltersViewResources.INSTANCE.style(); 
		style.ensureInjected();	
		
		findFilters();
	}
	
	@Override
	public void onStop() {
		this.eventBus = null;
		view.clear();
		view.setPresenter(null);
		view.setSelectionModel(null);
	}

	private void findFilters() {
		utils.findFilters(new Receiver<List<FilterType<?>>>() {
			@Override
			public void onSuccess(List<FilterType<?>> filters) {
				view.setFilters(filters);
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
