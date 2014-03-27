package ar.com.dcsys.gwt.assistance.client.ui.justification.widget;


import java.util.Date;

import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.gwt.assistance.client.ui.justification.person.JustificationPersonView;

import com.google.gwt.view.client.MultiSelectionModel;

public interface SelectionJustificationDateListWidget extends JustificationDateListWidget{
	public void setListSelectionModel(MultiSelectionModel<JustificationDate> selection);
	public void setPresenter (JustificationPersonView.Presenter p);
	public void search(Date start, Date end);
}
