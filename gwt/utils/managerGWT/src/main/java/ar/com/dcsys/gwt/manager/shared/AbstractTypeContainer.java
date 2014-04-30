package ar.com.dcsys.gwt.manager.shared;

public abstract class AbstractTypeContainer<T> implements TypeContainer<T> {

	private T t;
	
	@Override
	public T getValue() {
		return t;
	}
	
	@Override
	public void setValue(T t) {
		this.t = t;
	};
	
}
