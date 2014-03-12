package ar.com.dcsys.model;

import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;

public interface GroupsManager {
	
	public List<GroupType> findAllTypes();

	public List<Group> findAll() throws PersonException;
	
	public void addPersonTo(Group g, Person p) throws PersonException;
	public void removePersonFrom(Group g, Person p) throws PersonException;
	
	public void loadMembers(Group g) throws PersonException;
	
	public Group findById(String id) throws PersonException;
	public Group findByIdEager(String id) throws PersonException;
	
	public List<Group> findByPerson(Person p) throws PersonException;
	public List<Group> findByType(GroupType type) throws PersonException;
	
	
	public String persist(Group ou) throws PersonException;
	public void remove(Group ou) throws PersonException;	
	
	public Group findParent(Group g) throws PersonException;
	public List<Group> findAllParents(List<Group> groups) throws PersonException;
	public List<Group> findAllSons(List<Group> groups) throws PersonException;
		
	public void setParent(Group son, Group parent) throws PersonException;
}
