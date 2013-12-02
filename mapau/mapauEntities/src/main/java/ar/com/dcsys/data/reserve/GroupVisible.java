package ar.com.dcsys.data.reserve;

import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.exceptions.PersonException;


public interface GroupVisible extends Visible {
	
	public interface Params {
		/*
		 * TODO: saque el AuthException porque no se en que proyecto esta en la nueva estructura
		 */
		public List<Group> getLoggedUserGroups() throws PersonException;
	}
	
	
	public List<Group> getGroups();
	public void setGroups(List<Group> groups);

}
