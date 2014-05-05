package ar.com.dcsys.gwt.assistance.client.ui.justification.widget;

import java.util.List;

import ar.com.dcsys.data.justification.Justification;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionModel;

public interface JustificationListWidget extends IsWidget{
	public void setJustification(List<Justification> justifications);
	public void clear();
	public void setSelectionModel(SelectionModel<Justification> selection);

}
