package ar.com.dcsys.gwt.person.server;

import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.messages.shared.Transport;
import ar.com.dcsys.gwt.person.shared.GroupsManager;

public class GroupsManagerBean implements GroupsManager {

	private final ar.com.dcsys.model.GroupsManager groupsManager;
	
	@Override
	public void setTransport(Transport t) {
		// TODO Auto-generated method stub
		
	}
	
	@Inject
	public GroupsManagerBean(ar.com.dcsys.model.GroupsManager groupsManager) {
		this.groupsManager = groupsManager;
	}
	
	@Override
	public void findAllGroupTypes(Receiver<List<GroupType>> types) {
		try {
			List<GroupType> t = groupsManager.findAllTypes();
			types.onSuccess(t);
		} catch (Exception e) {
			types.onError(e.getMessage());
		}
	}

	@Override
	public void findAll(Receiver<List<Group>> groups) {
		try {
			List<Group> t = groupsManager.findAll();
			groups.onSuccess(t);
		} catch (Exception e) {
			groups.onError(e.getMessage());
		}
	}

}
