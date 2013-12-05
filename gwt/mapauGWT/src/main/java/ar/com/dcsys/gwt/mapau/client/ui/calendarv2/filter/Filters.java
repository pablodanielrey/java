package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.filter;

import java.util.List;

import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.FiltersView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.FiltersView.Presenter;
import ar.com.dcsys.gwt.mapau.client.ui.filter.widget.FilterWidget;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterType;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterValue;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class Filters extends Composite implements FiltersView {

	private static FiltersUiBinder uiBinder = GWT.create(FiltersUiBinder.class);

	interface FiltersUiBinder extends UiBinder<Widget, Filters> {
	}
	
	@UiField FiltersViewResources res;
	
	@UiField FilterWidget filtersWidget;
	@UiField Label saveFilters;
	@UiField Label cancelFilters;	
	
	private FiltersView.Presenter presenter;
	
	/*
	 * Selections necesarios para el FilterWidget
	 */
	
	private final SingleSelectionModel<FilterValue<?>> selectionFilter;
	private final SingleSelectionModel<FilterType<?>> selectionFilterType;

	public Filters() {
		initWidget(uiBinder.createAndBindUi(this));
		
		selectionFilter = new SingleSelectionModel<FilterValue<?>>();		
		filtersWidget.setSelectionFilter(selectionFilter);
		
		selectionFilterType = new SingleSelectionModel<FilterType<?>>();
		filtersWidget.setSelectionFilterType(selectionFilterType);
	}

	@Override
	public void setPresenter(Presenter p) {
		this.presenter = p;
	}

	@Override
	public void clear() {
		selectionFilter.clear();
		selectionFilterType.clear();
		filtersWidget.clear();
	}

	@Override
	public void setSelectionModel(MultiSelectionModel<FilterValue<?>> selection) {
		filtersWidget.setMultiSelectionFilter(selection);
	}

	@Override
	public void setFilters(List<FilterType<?>> filters) {
		filtersWidget.setFilterTypes(filters);
	}
	
	@UiHandler("saveFilters")
	public void onSaveFilters(ClickEvent event) {
		presenter.update();
		presenter.hideView(FiltersView.class.getName());
	}
	
	@UiHandler("cancelFilters")
	public void onCancelFilters(ClickEvent event) {
		presenter.hideView(FiltersView.class.getName());
	}	

}
