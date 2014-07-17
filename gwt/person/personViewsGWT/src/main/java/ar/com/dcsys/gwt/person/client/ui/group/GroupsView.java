package ar.com.dcsys.gwt.person.client.ui.group;

import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SingleSelectionModel;

public interface GroupsView extends IsWidget {
	
	public void setPresenter(Presenter p);
	public void clear();
	public void clearGroupData();
	
	public void setSelectionModel(SingleSelectionModel<Group> group);
	public void setInSelectionModel(SingleSelectionModel<Person> inSelection);
	public void setOutSelectionModel(SingleSelectionModel<Person> outSelection);
	public void setGroupTypeSelectionModel(SingleSelectionModel<GroupType> selection);
	
	public void setGroups(List<Group> groups);
	public void setInPersons(List<Person> in);
	public void setOutPersons(List<Person> out);	
	
	public List<GroupType> getSelectedGroupTypes();
	public void setSelectedGroupTypes(List<GroupType> types);
	public void setAllGroupTypes(List<GroupType> types);
	
	public void setParentSelectionModel(SingleSelectionModel<Group> selection);
	public void setParents(List<Group> groups);
	public void setParent(Group group);
	
	public List<PersonType> getSelectedPersonTypes();
	public void setAllPersonTypes(List<PersonType> types);
	
	public void setGroup(Group group);
	public String getMail();
	public String getName();
	
	public interface Presenter {
		public void updatePersons();
		public void createUpdate();
	}
	
}
