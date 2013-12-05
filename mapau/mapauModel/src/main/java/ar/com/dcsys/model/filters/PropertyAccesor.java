package ar.com.dcsys.model.filters;

import java.util.List;

import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.exceptions.FilterException;

public interface PropertyAccesor<T> {
	public boolean multipleValues();
	public T getProperty(ReserveAttemptDate ra) throws FilterException;
	public List<T> getProperties(ReserveAttemptDate ra) throws FilterException;
}
