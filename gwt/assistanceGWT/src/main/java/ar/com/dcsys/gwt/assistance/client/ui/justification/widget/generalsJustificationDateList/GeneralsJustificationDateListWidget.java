package ar.com.dcsys.gwt.assistance.client.ui.justification.widget.generalsJustificationDateList;

import java.util.List;

import ar.com.dcsys.data.justification.GeneralJustificationDate;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionModel;

public interface GeneralsJustificationDateListWidget extends IsWidget{
	public void setGeneralsJustificationDate(List<GeneralJustificationDate> dates);
	public void clear();
	public void setSelectionModel(SelectionModel<GeneralJustificationDate> selection);	
}
