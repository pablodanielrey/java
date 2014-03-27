package ar.com.dcsys.gwt.assistance.client.ui.justification.widget;

import java.util.List;

import ar.com.dcsys.data.justification.JustificationDate;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.MultiSelectionModel;

public interface JustificationDateListWidget extends IsWidget{

	public void setJustificationDates(List<JustificationDate> justificationDates);
	public void clear();
	public void setSelectionModel(MultiSelectionModel<JustificationDate> selection);
}
