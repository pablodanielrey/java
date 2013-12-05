package ar.com.dcsys.model.filters;

import java.util.Comparator;
import java.util.List;

import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.exceptions.FilterException;


public class PropertyFilter<T> implements ReserveAttemptDateFilter {

	private final T object;
	private final Comparator<T> comparator;
	private final PropertyAccesor<T> accesor;
	
	public PropertyFilter(T object, Comparator<T> comparator, PropertyAccesor<T> accesor) throws FilterException {
		this.object = object;
		this.comparator = comparator;
		this.accesor = accesor;
	}
	
	@Override
	public boolean filter(ReserveAttemptDate ra) throws FilterException {
		
		if (accesor.multipleValues()) {
			List<T> values = accesor.getProperties(ra);
			if (values == null || values.size() <= 0) {
				return false;
			}
			for (T t : values) {
				if (comparator.compare(object, t) == 0) {
					return true;
				}
			}
			return false;
			
		} else {
			
			T t = accesor.getProperty(ra);
			return (comparator.compare(object, t) == 0);
			
		}
	}

	@Override
	public Class getType() {
		return object.getClass();
	}

}
