package ar.com.dcsys.model;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupDAO;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class GroupsManagerBean implements GroupsManager {

	private final GroupDAO groupManager;
	private final PersonsManager personsManager;
	
	private LoadingCache<String,Group> groupCache;
	
//	@EJB Configuration config;
//	@EJB AuthsManager authsManager;
//	@Resource SessionContext ejbContext;
	
	@Inject
	public GroupsManagerBean(GroupDAO groupDAO, PersonsManager personsManager) {
		this.groupManager = groupDAO;
		this.personsManager = personsManager;
		createCaches();
	}
	
	/*
	@PreDestroy
	private void destroy() {
		groupCache.cleanUp();
		groupCache.invalidateAll();
		groupCache = null;
	}
	*/

	private void createCaches() {

		groupCache = CacheBuilder.newBuilder().maximumSize(1000).build(new CacheLoader<String,Group>() {
			@Override
			public Group load(String groupId) throws Exception {
				if (groupManager == null) {
					throw new PersonException("No se puede cargar : " + groupId + " debido a que groupManager == null");
				}
				return groupManager.findById(groupId);
			}
		});
		
	}
	
	
	@Override
	public List<GroupType> findAllTypes() {
		return groupManager.findAllTypes();
	}
	
	
	/**
	 * REtorna todos los grupos de determinado tipo a los que tiene acceso la persona que llama al método.
	 */
	@Override
	public List<Group> findByType(GroupType type) throws PersonException {

		if (type == null) {
			throw new PersonException("El tipo a buscar no puede ser nulo");
		}

		List<Group> groupsInType = new ArrayList<Group>(); 
		
		List<Group> groups = findAll();
		if (groups == null || groups.size() <= 0) {
			return groupsInType;
		}
		 
		for (Group g : groups) {
			List<GroupType> gts = g.getTypes();
			for (GroupType gt : gts) {
				if (type.equals(gt)) {
					groupsInType.add(g);
					break;
				}
			}
		}

		return groupsInType;

		/*
		
		try {
			// si soy super administrador entonces retorno todos los grupos sin restricciones de ou.
			if (authsManager.isUserInRole("ADMIN")) {
				return groupManager.findByType(type);
			}
			
			// busco la persona que esta logueada en el sistema.
			Person caller = ModelUtils.getCaller(ejbContext,authsManager,personsManager);
			if (caller == null) {
				throw new PersonException("Debe estar logueado dentro del contexto ejb para poder llamar a este método");
			}
	
			List<Group> access = ModelUtils.findGroupsWithAccess(this, caller);
			access = ModelUtils.filterGroupByType(access, type);
			
			return access;
			
		} catch (AuthException e) {
			throw new PersonException(e);
		}
		*/
	}

	
	@Override
	public List<Group> findAll() throws PersonException {
		List<String> ids = groupManager.findAllIds();
		if (ids == null || ids.size() <= 0) {
			return new ArrayList<Group>();
		}
		
		//try {
			List<Group> groups = new ArrayList<>();		
			for (String id : ids) {
			//for (Group g : groupCache.getAll(ids).values()) {
				Group g = groupManager.findById(id);
				groups.add(g);
			}
			return groups;
		/*} catch (ExecutionException e) {
			throw new PersonException(e.getCause().getMessage());
		}*/
		
		
		/* TODO: comentario viejo
		
		try {			
			// si soy super administrador entonces retorno todos los grupos sin restricciones de ou.
			if (authsManager.isUserInRole("ADMIN")) {
				if (!findAll) {
					List<Group> groups = groupManager.findAll();
					updateCache(groups);
					findAll = true;
				}
				
				return cache.getAll(); 
			}
			
			
			// busco la persona que esta logueada en el sistema.
			Person caller = ModelUtils.getCaller(ejbContext,authsManager,personsManager);
			if (caller == null) {
				throw new PersonException("Debe estar logueado dentro del contexto ejb para poder llamar a este método");
			}
	
			List<Group> access = ModelUtils.findGroupsWithAccess(this, caller);
			return access;
			
		} catch (AuthException e) {
			throw new PersonException(e);
		}
		
		*/
	}

	/**
	 * Agrega una persona a un grupo determinado.
	 */
	@Override
	public void addPersonTo(Group g, Person p) throws PersonException {
		
		if (g == null) {
			throw new PersonException("group == null");
		}
		
		String id = g.getId();
		if (id == null) {
			throw new PersonException("Grupo Sin Id");
		}		
		
		if (p == null) {
			throw new PersonException("person == null");
		}

		groupCache.invalidate(id);
		groupManager.addPersonToGroup(g, p);
	}
	
	/**
	 * Elimina una persona de un grupo.
	 * si no existe ninguna persona tira una excepcion.
	 */
	@Override
	public void removePersonFrom(Group g, Person p) throws PersonException {
		
		if (g == null) {
			throw new PersonException("Group == null");
		}
		
		if (p == null) {
			throw new PersonException("Person == null");
		}
		
		String id = g.getId();
		if (id == null) {
			throw new PersonException("Grupo Sin Id");
		}
		
		groupCache.invalidate(id);
		groupManager.removePersonFromGroup(g, p);
	}	
	
	

	/**
	 * Obtiene el grupo identificado por el id pasado como parámetro.
	 * el grupo retornado tiene cargado los miembros.
	 */
	@Override
	public Group findByIdEager(String id) throws PersonException {
		Group g = findById(id);
		if (g.getPersons() == null || g.getPersons().size() <= 0) {
			loadMembers(g);
		}
		return g;
	}

	/**
	 * Obtiene el grupo identificado por el id pasado por parámetro.
	 * el grupo retornado puede tener cargado los miembros si es que ya fue cargado alguna vez en la cache.
	 * en caso contrario no tiene cargado ningun miembro.
	 */
	@Override
	public Group findById(String id) throws PersonException {
		
		if (id == null) {
			throw new PersonException("id == null");
		}
		
		/*try {
			Group g = groupCache.get(id);
			return g;
		} catch (ExecutionException e) {
			throw new PersonException(e.getCause().getMessage());
		}*/

		return groupManager.findById(id);
	}

	
	/**
	 * Carga los miembros del grupo.
	 */
	@Override
	public void loadMembers(Group g) throws PersonException {
		List<String> idsPersons = groupManager.getMembersIds(g);
		List<Person> persons = new ArrayList<>();
		for (String id : idsPersons) {
			Person p = personsManager.findById(id);
			if (p != null) {
				persons.add(p);
			}
		}
		g.setPersons(persons);
	}
	
	/**
	 * Busca los grupos por persona y le carga los miembros
	 */
	@Override
	public List<Group> findByPerson(Person p) throws PersonException {

		List<Group> groups = new ArrayList<Group>();

		List<String> ids = groupManager.findByPerson(p);
		if (ids == null || ids.size() <= 0) {
			return groups;
		}
		
		for (String id : ids) {
			Group g = findByIdEager(id);
			groups.add(g);
		}
		
		return groups;
	}


	/**
	 * Persiste un grupo y sus datos.
	 */
	@Override
	public String persist(Group g) throws PersonException {

		if (g == null) {
			throw new PersonException("Group == null");
		}
		
		String id = g.getId();
		if (id != null) {
			groupCache.invalidate(id);
			
		}		

		return groupManager.persist(g);
	}

	
	/**
	 * elimina un grupo de la base e invalida la cache correspondiente a ese grupo.
	 */
	@Override
	public void remove(Group g) throws PersonException {
		if (g == null) {
			throw new PersonException("Group == null");
		}
		
		String id = g.getId();
		if (id == null) {
			throw new PersonException("Group.id == null");
		}

		groupCache.invalidate(id);
		groupManager.remove(g);
	}


}
