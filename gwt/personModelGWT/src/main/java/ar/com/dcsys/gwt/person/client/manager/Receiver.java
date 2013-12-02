package ar.com.dcsys.gwt.person.client.manager;

public interface Receiver<T> {
	
	public void onSuccess(T t);
	public void onFailure(Throwable t);

}
