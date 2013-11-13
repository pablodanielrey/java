package ar.com.dcsys.data.group;

import java.util.List;
import java.util.logging.Logger;

import ar.com.dcsys.data.group.backend.GroupBackEnd;
import ar.com.dcsys.group.entities.Group;
import ar.com.dcsys.group.entities.types.GroupType;
import ar.com.dcsys.person.PersonException;
import ar.com.dcsys.person.entities.Person;


public class GroupManagerBean implements GroupManager {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(GroupManagerBean.class.getName());

	private final GroupBackEnd backEnd;
	
	public GroupManagerBean(GroupBackEnd backEnd) {
		this.backEnd = backEnd;
	}
	
	
	@Override
	public List<String> findByType(GroupType type) throws PersonException {
		return backEnd.findByType(type);
	}
	
	@Override
	public List<GroupType> findAllTypes() {
		return backEnd.findAllTypes();
	}
	
	@Override
	public List<Group> findAll() throws PersonException {
		return backEnd.findAll();
	}

	@Override
	public List<String> findAllIds() throws PersonException {
		return backEnd.findAllIds();
	}
	
	/**
	 * Encuentra los padres inmediatamente superiores a la lista de grupos dada.
	 * Los padres pueden ser independientes, o sea pueden no ser padres de TODOS los grupos pasados como parámetros.
	 */
	@Override
	public List<String> findAllParents(List<Group> groups) throws PersonException {
		return backEnd.findAllParents(groups);
	}
	
	/**
	 * Carga dentro del grupo el padre, y lo retorna.
	 */
	@Override
	public String findParent(Group g) throws PersonException {
		return backEnd.findParent(g);
	}
	
	/**
	 * Encuentra los hijos inmediatamente inferioes a la lsita de grupos dada.
	 * Los hijos pueden ser hijos de 1 o varios grupos, no necesariamente son hijos de todos los grupos a la vez.
	 */
	@Override
	public List<String> findAllSons(List<Group> groups) throws PersonException {
		return backEnd.findAllSons(groups);
	}

	@Override
	public void setParent(Group son, Group parent) throws PersonException {
		backEnd.setParent(son, parent);
	}
	
	@Override
	public Group findById(String id) throws PersonException {
		return backEnd.findById(id);
	}

	@Override
	public void loadMembers(Group g) throws PersonException {
		backEnd.loadMembers(g);
	}
	
	@Override
	public List<String> findByPerson(Person p) throws PersonException {
		return backEnd.findByPerson(p);
	}

	@Override
	public String persist(Group group) throws PersonException {
		return backEnd.persist(group);
	}

	@Override
	public void remove(Group group) throws PersonException {
		backEnd.remove(group);
	}

	@Override
	public void addPersonToGroup(Group g, Person p) throws PersonException {
		backEnd.addPersonToGroup(g, p);
	}
	
	@Override
	public void removePersonFromGroup(Group g, Person p) throws PersonException {
		backEnd.removePersonFromGroup(g, p);
	}
	
}
