package ar.com.dcsys.data.group;

import java.io.Serializable;
import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;

public interface GroupDAO extends Serializable {

	public List<GroupType> findAllTypes();
	
	public List<Group> findAll() throws PersonException;
	public List<String> findAllIds() throws PersonException;
	
	public void loadMembers(Group g) throws PersonException;
	
	public Group findById(String id) throws PersonException;
	public List<String> findByPerson(Person p) throws PersonException;
	public List<String> findByType(GroupType type) throws PersonException;
	
	public void addPersonToGroup(Group g, Person p) throws PersonException;
	public void removePersonFromGroup(Group g, Person p) throws PersonException;	
	
	public String persist(Group ou) throws PersonException;
	public void remove(Group ou) throws PersonException;
	
	public String findParent(Group group) throws PersonException;
	public List<String> findAllParents(List<Group> groups) throws PersonException;
	public List<String> findAllSons(List<Group> groups) throws PersonException;

	public void setParent(Group son, Group parent) throws PersonException;
	
	public void initialize() throws PersonException;
	public void destroy() throws PersonException;
	
	public interface Params {
		public List<Person> findPersonByMemberField(String userName) throws PersonException;
		public String getMemberFieldContent(Person person) throws PersonException;
	}
	
}
