package ar.com.dcsys.gwt.mapau.client.filter;

import java.util.List;

import ar.com.dcsys.gwt.mapau.shared.data.filter.Filter;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterType;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterValue;

public class FilterTypeDate implements FilterType<DateValue> {

	private final List<Filter<?>> values;
	private boolean visible;
	
	private class FilterValueDate implements FilterValue<DateValue> {
		private FilterDate selected;
		private FilterType<?> filterType;
		private boolean visible;
		
		public FilterValueDate (FilterType<?> type) {
			this.filterType = type;
			this.visible = false;
		}
		
		@Override
		public FilterType<?> getFilterType() {
			return filterType;
		}
		
		@Override
		public String getName() {
			return "Fecha";
		}
		
		@Override
		public Filter<DateValue> getSelected() {
			return selected;
		}
		
		@Override
		public void setSelected(Filter<?> filter) {
			if (filter instanceof FilterDate) {
				this.selected = (FilterDate) filter;
			}
		}
		
		@Override
		public boolean isVisible() {
			return visible;
		}
		
		@Override
		public void setVisible(boolean v) {
			this.visible = v;
		}
		
	}
	
	public FilterTypeDate() {
		values = null;
		visible = false;
	}
	
	@Override
	public String getName() {
		return "Fecha";
	}

	@Override
	public FilterValue<DateValue> createNewFilter() {
		return new FilterValueDate(this);
	}

	@Override
	public List<Filter<?>> getValues() {
		return values;
	}

	@Override
	public String getClassNameFilter() {
		return FilterDate.class.getName();
	}

	
	@Override
	public boolean isVisible() {
		return visible;
	}
	
	@Override
	public void setVisible(boolean v) {
		this.visible = v;
	}
}
