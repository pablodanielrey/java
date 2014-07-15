package ar.com.dcsys.gwt.person.client.manager;

import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.gwt.manager.shared.Receiver;

public interface GroupsManager {

	public void findAllGroupTypes(Receiver<List<GroupType>> types);
	public void findAll(Receiver<List<Group>> groups);
	
}
