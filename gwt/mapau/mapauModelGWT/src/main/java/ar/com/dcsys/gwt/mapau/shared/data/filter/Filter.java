package ar.com.dcsys.gwt.mapau.shared.data.filter;

import ar.com.dcsys.data.filter.TransferFilterType;
import ar.com.dcsys.gwt.mapau.shared.data.filter.transfer.TransferableFilter;



public interface Filter<T> extends TransferableFilter {

	public T getValue();
	public void setValue(T value);
	
	public String toString();
	public int compareTo(Filter<?> o2);
	
	public TransferFilterType getType();
	
}
