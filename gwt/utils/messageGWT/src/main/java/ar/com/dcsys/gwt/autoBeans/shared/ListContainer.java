package ar.com.dcsys.gwt.autoBeans.shared;

import java.util.List;

import com.google.web.bindery.autobean.shared.Splittable;

public interface ListContainer<T> {
	public List<T> getList();
	public void setList(List<T> list);
	
	public Splittable getElementAs();
	
	public T getElementAs(Class<T> clazz, int index);
	public List<T> getElements(Class<T> clazz);
	
}
