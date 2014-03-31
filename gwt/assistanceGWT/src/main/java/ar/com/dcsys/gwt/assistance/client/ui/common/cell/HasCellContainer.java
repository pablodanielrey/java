package ar.com.dcsys.gwt.assistance.client.common.cell;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;

public class HasCellContainer<T> implements HasCell<T,T> {
	
	private final Cell<T> c;
	
	public HasCellContainer(Cell<T> c) {
		this.c = c;
	}
	
	@Override
	public Cell<T> getCell() {
		return c;
	}
	
	@Override
	public FieldUpdater<T, T> getFieldUpdater() {
		return null;
	}
	
	public T getValue(T object) {
		return object;
	};
}
