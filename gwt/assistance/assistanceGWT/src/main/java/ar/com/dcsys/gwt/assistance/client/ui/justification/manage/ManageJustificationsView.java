package ar.com.dcsys.gwt.assistance.client.ui.justification.manage;

import java.util.List;

import ar.com.dcsys.data.justification.Justification;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionModel;

public interface ManageJustificationsView extends IsWidget{

	public void clear();
	public void clearData();
	public void setPresenter(Presenter p);
	
	public void setJustifications (List<Justification> justifications);
	public void setSelectionJustification (SelectionModel<Justification> selection);	
	
	public void setEnabled(boolean b);
	public void setJustification(Justification justification);
	
	public String getCode();
	public String getDescription();
	
	public void setActionText(String action);
	
	public interface Presenter {
		public void remove();
		public void cancel();
		public void commit();
	}
}
