package ar.com.dcsys.gwt.person.client.manager;

import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.ws.client.WebSocket;

import com.google.gwt.core.client.GWT;

public class GroupsManagerBean implements GroupsManager {

	private final WebSocket ws;
	private final ar.com.dcsys.gwt.person.shared.GroupsManager groupsManager = GWT.create(ar.com.dcsys.gwt.person.shared.GroupsManager.class);
	
	@Inject
	public GroupsManagerBean(WebSocket ws) {
		this.ws = ws;
		groupsManager.setTransport(ws);
	}
	
	@Override
	public void findAllGroupTypes(Receiver<List<GroupType>> types) {
		groupsManager.findAllGroupTypes(types);
	}

	@Override
	public void findAll(Receiver<List<Group>> groups) {
		groupsManager.findAll(groups);
	}

}
