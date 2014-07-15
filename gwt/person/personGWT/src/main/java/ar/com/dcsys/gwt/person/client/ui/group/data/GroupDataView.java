package ar.com.dcsys.gwt.person.client.ui.group.data;

import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SingleSelectionModel;

public interface GroupDataView extends IsWidget {

	public void setPresenter(Presenter presenter);
	public void clear();
	public void clearData();
	
	public List<GroupType> getSelectedGroupTypes();
	public void setSelectedGroupTypes(List<GroupType> types);
	
	public void setGroupTypes(List<GroupType> types);
	
	public void setParents(List<Group> groups);
	public void setParent(Group group);
	public Group getGroupParent();
	
	public void setGroup(Group group);
	public String getMail();
	public String getName();	
	
	public interface Presenter {
		public void setSelectionModel(SingleSelectionModel<Group> selection);
		public void persist();
	}
}
