package ar.com.dcsys.gwt.assistance.client.ui.period.cells;

import ar.com.dcsys.data.justification.JustificationDate;

public interface JustificationActionCellPresenter<T> {
	public JustificationDate getJustifications(T p);
	public void deleteJustification(JustificationDate j);
	public void justify();

}
