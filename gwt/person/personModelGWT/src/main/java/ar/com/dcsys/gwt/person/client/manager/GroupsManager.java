package ar.com.dcsys.gwt.person.client.manager;

import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.manager.shared.Receiver;

public interface GroupsManager {

	public void findAllGroupTypes(Receiver<List<GroupType>> types);
	public void findAll(Receiver<List<Group>> groups);
	
	public void persist(Group g, Receiver<String> receiver);
	public void addPersonTo(Group g, Person p, Receiver<Void> receiver);
	public void removePersonFrom(Group g, Person p, Receiver<Void> receiver);
	
}
