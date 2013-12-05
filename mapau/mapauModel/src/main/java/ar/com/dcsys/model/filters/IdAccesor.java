package ar.com.dcsys.model.filters;

import ar.com.dcsys.exceptions.FilterException;

public interface IdAccesor<T> {
	public String getId(T object) throws FilterException;
}
