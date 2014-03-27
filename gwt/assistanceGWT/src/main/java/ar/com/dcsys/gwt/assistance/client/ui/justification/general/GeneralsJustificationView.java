package ar.com.dcsys.gwt.assistance.client.ui.justification.general;


import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.GeneralJustificationDateProvider;
import ar.com.dcsys.data.justification.Justification;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public interface GeneralsJustificationView extends IsWidget{

	public void setPresenter(Presenter p);
	public void setProvider(GeneralJustificationDateProvider p);
	
	public void clear();
	public void clearData();	
	public void clearGeneralJustificationDateList();
	public void clearNotes();
	
	public void setTypes(List<Justification> types);
	public List<GeneralJustificationDate> getDates();
	public void setTypesSelectionModel(SingleSelectionModel<Justification> selection);	
	public void setGeneralJustificationSelection(MultiSelectionModel<GeneralJustificationDate>generalJustificationSelection);
	public void setGeneralJustificationDateList (List<GeneralJustificationDate> justifications);	

	public String getNotes();
	public Date getStart();
	public Date getEnd();
	
	public interface Presenter {
		public void persist();
		public void search(Date start, Date end);
		public void delete();
	}	
	
}
