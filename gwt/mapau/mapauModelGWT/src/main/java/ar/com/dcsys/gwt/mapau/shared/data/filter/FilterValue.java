package ar.com.dcsys.gwt.mapau.shared.data.filter;

public interface FilterValue<T> {

	public void setSelected(Filter<?> selected);
	public Filter<T> getSelected();
	
	public String getName();
	public FilterType<?> getFilterType();
	
	public boolean isVisible();
	public void setVisible(boolean v);
	
}
