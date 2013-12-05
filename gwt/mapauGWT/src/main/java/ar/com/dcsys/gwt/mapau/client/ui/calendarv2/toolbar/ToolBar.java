package ar.com.dcsys.gwt.mapau.client.ui.calendarv2.toolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.gwt.mapau.client.filter.DateValue;
import ar.com.dcsys.gwt.mapau.client.filter.FilterDate;
import ar.com.dcsys.gwt.mapau.client.filter.FilterTypeDate;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.CalendarView.CalendarType;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.FiltersView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.ToolBarView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.ToolBarView.Presenter;
import ar.com.dcsys.gwt.mapau.shared.data.filter.Filter;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterType;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterValue;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

public class ToolBar extends Composite implements ToolBarView {

	private static ToolBarUiBinder uiBinder = GWT.create(ToolBarUiBinder.class);

	interface ToolBarUiBinder extends UiBinder<Widget, ToolBar> {
	}
	
	@UiField ToolBarViewResources res;

	@UiField(provided=true) DateBox date;
	@UiField(provided=true) ValueListBox<CalendarType> calendarViewType;
	@UiField Label showFilter;
	@UiField FlowPanel datePanel;
	
	private ToolBarView.Presenter presenter; 
	
	private MultiSelectionModel<FilterValue<?>> filtersSelection;
	
	private FilterValue<DateValue> filterValue = null;;
	
	private SingleSelectionModel<CalendarType> calendarTypeSelection;
	
	/**
	 * Handler que mantiene actualizada la selección del ValueListBox con los cambios en el selectionModel.
	 */
	private final Handler typeSelectionHandler = new Handler() {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			CalendarType selected = calendarTypeSelection.getSelectedObject();
			if (selected == null) {
				calendarViewType.setValue(null);
			} else {
				calendarViewType.setValue(selected);
			}
		}
	};
	
	
	
	/**
	 * Handler que mantiene actualizado la selección del ValueListBox con el selectionModel
	 */
	private final ValueChangeHandler<CalendarType> typeValueHandler = new ValueChangeHandler<CalendarType>() {
		@Override
		public void onValueChange(ValueChangeEvent<CalendarType> event) {
			if (calendarTypeSelection != null) {
				CalendarType type = calendarViewType.getValue();
				calendarTypeSelection.setSelected(type, true);
				
				/*
				 * Aca tengo que setear la fecha
				 */
				if (date == null) {
					return;
				}
				Date d = date.getValue();
				
				Date start= new Date(d.getTime());
				start.setDate(1);
				start.setHours(0);
				start.setMinutes(0);
				start.setSeconds(0);
				
				long endMilis = 1000l * 60l * 60l * 24l;
				if (type.equals(CalendarType.MONTHLY)) {
					endMilis = endMilis * 31l;
				} else {
					endMilis = endMilis * 7l;
				}
				
				Date end = new Date(start.getTime() + endMilis);
				end.setHours(23);
				end.setMinutes(59);
				end.setSeconds(59);				
				
				setFilterDate(start, end);				
			}
		}
	};	
	
	private void createCalendarType() {
		calendarViewType = new ValueListBox<CalendarType>(new Renderer<CalendarType>() {
			
			private String getValue(CalendarType type) {
				if (type == null) {
					return "Nulo";
				}
				return type.getDescription();
			}
			
			@Override
			public String render(CalendarType type) {
				return getValue(type);
			}
			@Override
			public void render(CalendarType type, Appendable appendable) throws IOException {
				if (type == null) {
					return;
				}
				appendable.append(getValue(type));
			}
		});
		
		calendarViewType.addValueChangeHandler(typeValueHandler);
	}
	
	
	public ToolBar() {
		createDate();
		createCalendarType();
		initWidget(uiBinder.createAndBindUi(this));
		setVisibleDate(false);
	}
	
	private void setVisibleDate(boolean v) {
		String style;
		if (v) {
			style = res.style().datePanel();
		} else {
			style = res.style().datePanelHide();
		}
		
		datePanel.setStyleName(style);
	}

	private void createDate() {
		final DateTimeFormat dateF = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
		DatePicker dp = new DatePicker();
		dp.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				if (presenter == null || filterValue == null) {
					return;
				}
				
				Date start= event.getValue();
				start.setHours(0);
				start.setMinutes(0);
				start.setSeconds(0);
				
				Long week = 1000l * 60l * 60l * 24l * 7l;
				Date end = new Date(start.getTime() + week);
				end.setHours(23);
				end.setMinutes(59);
				end.setSeconds(59);				
				
				setFilterDate(start, end);
			}
		});
		
		date = new DateBox(dp, new Date(), new DefaultFormat(dateF));	
	}
	
	public void setFilterDate(Date start, Date end) {

						
		Filter<DateValue> filterDate = filterValue.getSelected();
		DateValue value = null;
		if (filterDate == null) {
			filterDate = new FilterDate(value);
		}

		 if (value == null) {
			 value = new DateValue();
		 }
		
		value.setStart(start);
		value.setEnd(end);
		 
		filterDate.setValue(value);
		
		filterValue.setSelected(filterDate);
		filtersSelection.setSelected(filterValue, true);
		
		presenter.update();		
	}
	
	
	@Override
	public void setSelectionModel(MultiSelectionModel<FilterValue<?>> selection) {
		filtersSelection = selection;
	}

	@Override
	public void setFilters(List<FilterType<?>> filters) {
		if (filters == null) {
			return;
		}
				
		for (FilterType<?> type : filters) {
			if (type instanceof FilterTypeDate) {				
				filterValue = ((FilterTypeDate)type).createNewFilter();
				setVisibleDate(true);
				return;
			}
		}
	}

	@Override
	public void setPresenter(Presenter p) {
		this.presenter = p;
	}

	@Override
	public void clear() {
		filterValue = null;
		presenter.hideView(FiltersView.class.getName());
		setVisibleDate(false);
		clearCalendarType();
	}
	
	private void clearCalendarType() {
		calendarViewType.setAcceptableValues(new ArrayList<CalendarType>());
	}
	
	@UiHandler("showFilter")
	public void onShowFilter(ClickEvent event) {
		presenter.showView(FiltersView.class.getName());	
	}
	
	@Override
	public void setCalendarTypeSelectionModel(SingleSelectionModel<CalendarType> selection) {
		this.calendarTypeSelection = selection;
		selection.addSelectionChangeHandler(typeSelectionHandler);
	}
	
	@Override
	public void setCalendarTypes(List<CalendarType> types) {		
		if (calendarTypeSelection != null) {
			CalendarType selected = calendarTypeSelection.getSelectedObject();
			this.calendarViewType.setValue(selected);
		}
		
		if (types == null || types.size() <= 0) {
			types = new ArrayList<CalendarType>();
		}
		
		this.calendarViewType.setAcceptableValues(types);
	}
	

}
