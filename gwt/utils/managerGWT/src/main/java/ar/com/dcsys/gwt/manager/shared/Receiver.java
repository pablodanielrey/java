package ar.com.dcsys.gwt.manager.shared;

public interface Receiver<T> {

	public void onSuccess(T t);
	public void onError(String error);
	
}
