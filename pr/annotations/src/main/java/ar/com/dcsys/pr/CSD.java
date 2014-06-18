package ar.com.dcsys.pr;

public interface CSD<T> {

	public String toJson(T o);
	public T read(String json);
	
}
