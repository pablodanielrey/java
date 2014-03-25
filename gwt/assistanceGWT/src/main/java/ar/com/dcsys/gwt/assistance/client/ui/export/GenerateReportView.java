package ar.com.dcsys.gwt.assistance.client.ui.export;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.gwt.assistance.client.ui.period.PERIODFILTER;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SingleSelectionModel;

public interface GenerateReportView extends IsWidget{
	
	public void setPresenter (Presenter p);	
	public void clear();
	
	public void setGroup(List<Group> groups);
	public void setGroupSelection(SingleSelectionModel<Group> selection);
	
	public void setGroupTypes(List<GroupType> types);
	public void setGroupTypesSelection(SingleSelectionModel<GroupType> selection);

	
	public Date getStart();
	public Date getEnd();
	public void setExporedData(String data);
	
	
	public void setReportSelectionModel(SingleSelectionModel<PERIODFILTER> selection);
	public void setReports(List<PERIODFILTER> reports);	
	
	
	public interface Presenter {
		public void generateReport();		
	}
}
