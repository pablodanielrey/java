package ar.com.dcsys.gwt.mapau.client.ui.filter.widget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.dcsys.gwt.mapau.client.ui.common.SelectionObjectCell;
import ar.com.dcsys.gwt.mapau.shared.data.filter.Filter;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterType;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterValue;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class FilterWidget extends Composite implements IsWidget {

	private static FilterListWidgetUiBinder uiBinder = GWT.create(FilterListWidgetUiBinder.class);

	interface FilterListWidgetUiBinder extends UiBinder<Widget, FilterWidget> {
	}
		
	
	private SingleSelectionModel<FilterType<?>> selectionCreateFilterType;
	private MultiSelectionModel<FilterValue<?>> filtersAdding;
	
	@UiField(provided=true)ValueListBox<FilterType<?>> filterType;	
	@UiField(provided=true)DataGrid<FilterValue<?>> filters;
	@UiField Label addFilter;
	
			
	private Map<String,List<Filter<?>>> valuesFilterByType = new HashMap<String,List<Filter<?>>>();
	
	public FilterWidget() {		
		createFilters();
		createFilterType();
		initWidget(uiBinder.createAndBindUi(this));		
	}
	
	

	public void createFilters() {
		
		filters = new DataGrid<FilterValue<?>>();
		
		ActionCell<FilterValue<?>> removeCell = new ActionCell<FilterValue<?>>("Eliminar", new ActionCell.Delegate<FilterValue<?>>() {
			@Override
			public void execute(FilterValue<?> object) {
				filtersAdding.setSelected(object, false);
				setFilters();
			}
		});
		
		Column<FilterValue<?>, FilterValue<?>> removeColumn = new IdentityColumn<FilterValue<?>>(removeCell);
		
		TextColumn<FilterValue<?>> nameFilter = new TextColumn<FilterValue<?>>(){
			@Override
			public String getValue(FilterValue<?> filter) {
				return filter.getName();
			}
		}; 

		SelectionObjectCell.Renderer<Filter<?>> render = new SelectionObjectCell.Renderer<Filter<?>>() {			
			@Override
			public String getValue(Filter<?> filter) {
				return filter.toString();
			}
		};
		
		Comparator<Filter<?>> comparator = new Comparator<Filter<?>>() {			
			@Override
			public int compare(Filter<?> o1, Filter<?> o2) {
				return o1.compareTo(o2);
			}
		};
		
		SelectionObjectCell.DataProvider<Filter<?>> provider = new SelectionObjectCell.DataProvider<Filter<?>>() {
			
			public List<Filter<?>> getValues(Filter<?> value) {
				if (value == null) {
					return new ArrayList<Filter<?>>();
				}
				String nameClass = value.getClass().getName();
				List<Filter<?>> values = valuesFilterByType.get(nameClass);
				if (values == null) {
					values = new ArrayList<Filter<?>>();
				}
				return values;
			}
		};
		
		SelectionObjectCell<Filter<?>> filterCell = new SelectionObjectCell<Filter<?>>(render, comparator, provider);
		
		Column<FilterValue<?>, Filter<?>> filterColumn = new Column<FilterValue<?>, Filter<?>>(filterCell) {
			@Override
			public Filter<?> getValue(FilterValue<?> filterValue) {
				return filterValue.getSelected();
			}
		};
		
		filterColumn.setFieldUpdater(new FieldUpdater<FilterValue<?>,Filter<?>>() {
			@Override
			public void update(int index, FilterValue<?> object, Filter<?> value) {
				object.setSelected(value);
			}
		});
	
		filters.addColumn(removeColumn,"Eliminar");
		filters.addColumn(nameFilter,"Filtro");
		filters.addColumn(filterColumn,"Valor");			
	}
	
	public void createFilterType() {
		filterType = new ValueListBox<FilterType<?>>(new Renderer<FilterType<?>>() {	
			
			private String getValue(FilterType<?> filterType) {
				if (filterType == null) {
					return "nulo";
				}
				return filterType.getName();
			}
			
			@Override
			public String render(FilterType<?> filterType) {
				return getValue(filterType);
			}

			@Override
			public void render(FilterType<?> object, Appendable appendable) throws IOException {
				if (object == null) {
					return;
				}
				appendable.append(getValue(object));
			}
		});
		
		filterType.addValueChangeHandler(new ValueChangeHandler<FilterType<?>>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<FilterType<?>> event) {
				if (selectionCreateFilterType != null) {
					FilterType<?> ft = event.getValue();
					if (ft != null) {
						selectionCreateFilterType.setSelected(ft, true);
					}
				}
			}
		});
	}
	
	
	public void setFilterTypes(List<FilterType<?>> values) {
		
		List<FilterType<?>> types = new ArrayList<FilterType<?>>();
		
		if (values != null && values.size() > 0) {
			
			for (FilterType<?> filter : values) {
				if (filter.isVisible()) {
					types.add(filter);
				}
			}
			
			if (types.size() > 0) {
				FilterType<?> value = types.get(0);
				this.filterType.setValue(value);
				if (selectionCreateFilterType != null) {
					selectionCreateFilterType.setSelected(value, true);
				}
			}
		}
		
		this.filterType.setAcceptableValues(types);
	}	
	
	public void setSelectionFilterType(SingleSelectionModel<FilterType<?>> selection) {		
		this.selectionCreateFilterType = selection;
	}
	
	public void setMultiSelectionFilter(MultiSelectionModel<FilterValue<?>> selection) {
		this.filtersAdding = selection;
	}
		
	public void setSelectionFilter(SingleSelectionModel<FilterValue<?>> selection) {
		filters.setSelectionModel(selection);
	}
	
	@UiHandler("addFilter")
	public void onAddFilter(ClickEvent event) {
		if (selectionCreateFilterType == null || filtersAdding == null) {
			return;
		}
		FilterType<?> selectedFilterType = selectionCreateFilterType.getSelectedObject(); 
		if (selectedFilterType == null) {
			return;
		}
		FilterValue<?> newFilterValue = selectedFilterType.createNewFilter();
		
		String nameClass = selectedFilterType.getClassNameFilter();
		List<Filter<?>> values = valuesFilterByType.get(nameClass);
		
		if (values == null) {
			values = selectedFilterType.getValues();
			valuesFilterByType.put(nameClass, values);
		}
		
		if (values != null && values.size() > 0) {
			newFilterValue.setSelected(values.get(0));
		}
		
		filtersAdding.setSelected(newFilterValue, true);	
		
		setFilters();
		
	}
	
	private void setFilters() {
		
		List<FilterValue<?>> selectedFilters = new ArrayList<FilterValue<?>>(filtersAdding.getSelectedSet());
		
		for (FilterValue<?> filterValue : selectedFilters) {
			if(!filterValue.isVisible()) {
				selectedFilters.remove(filterValue);
			}
		}		
		
		this.filters.setRowData(selectedFilters);
		
	}
	
	public void clear() {
		filters.setRowCount(0);
		filters.setRowData(new ArrayList<FilterValue<?>>());
		
		filterType.setAcceptableValues(null);
		filterType.setValue(null);
	}
	
	

}
