package ar.com.dcsys.gwt.mapau.shared.data.filter;

import java.util.List;

public interface FilterType<T> {
	
	public String getName();
	public FilterValue<T> createNewFilter();
	public List<Filter<?>> getValues();
	
	public String getClassNameFilter();	
	
	public boolean isVisible();
	public void setVisible(boolean v);
}
