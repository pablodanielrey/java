package ar.com.dcsys.gwt.person.client.ui.group.list;

import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SingleSelectionModel;

public interface GroupListView extends IsWidget {
	
	public void setPresenter(Presenter presenter);	
	public void clear();
	
	public void setSelectionModel(SingleSelectionModel<Group> group);
	public void setGroups(List<Group> groups);
	
	public void setGroupTypeSelectionModel(SingleSelectionModel<GroupType> selection);
	public void setGroupTypes(List<GroupType> types);
	
	public interface Presenter {
		public void setSelectionModel(SingleSelectionModel<Group> selection);
		public void findGroups();
	}

}
