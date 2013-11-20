package ar.com.dcsys.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonDAO;
import ar.com.dcsys.data.person.types.PersonType;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.exceptions.PersonNotFoundException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;


@Singleton
public class PersonsManagerBean implements PersonsManager {

	private static Logger logger = Logger.getLogger(PersonsManagerBean.class.getName());
	
	private LoadingCache<String,Person> personCache;
	private LoadingCache<String,String> dnisCache;
	private LoadingCache<PersonType,List<String>> personTypesCache;
	
	private final PersonDAO personDAO;
	
	@Inject
	public PersonsManagerBean(PersonDAO personDAO) {
		this.personDAO = personDAO;
		createCaches();
	}
	
	/*
	@PreDestroy
	private void destroy() {
		
		dnisCache.cleanUp();
		dnisCache.invalidateAll();
		dnisCache = null;
		
		personCache.cleanUp();
		personCache.invalidateAll();
		personCache = null;
	}
	*/

	
	private void createCaches() {
		//personCache = CacheBuilder.newBuilder().maximumSize(500).expireAfterWrite(20,TimeUnit.MINUTES).build(new CacheLoader<String,Person>() {
		personCache = CacheBuilder.newBuilder().maximumSize(27000).build(new CacheLoader<String,Person>() {
			@Override
			public Person load(String person_id) throws Exception {
				Person person = personDAO.findById(person_id);
				if (person == null) {
					throw new PersonNotFoundException();
				}
				return person;
			}
			
			/*
			 * VER COMO AJUSTAMOS EL TEMA DE CUANDO NO EXISTE ALGUNA KEY.
			 * 
			@Override
			public Map<String, Person> loadAll(Iterable<? extends String> keys) throws Exception {
				if (personManager == null) {
					throw new PersonException("No se puede cargar las personas debido a que no existe personManager");
				}
				List<Person> persons = personManager.findAll(keys);
				Map<String,Person> mpersons = new HashMap<String,Person>();
				for (Person p : persons) {
					mpersons.put(p.getId(),p);
				}
				return mpersons;
			}
			*/
		});
		
		// creo la cache para dni --> id
		dnisCache = CacheBuilder.newBuilder().maximumSize(1000).build(new CacheLoader<String,String>() {
			@Override
			public String load(String key) throws Exception {
				String id = personDAO.findIdByDni(key);
				if (id == null) {
					throw new PersonNotFoundException();
				}
				return id;
			}
		});
		
		personTypesCache = CacheBuilder.newBuilder().maximumSize(500).build(new CacheLoader<PersonType,List<String>>() {
			@Override
			public List<String> load(PersonType key) throws Exception {
				List<String> ids = personDAO.findAllIdsBy(Arrays.asList(key));
				if (ids == null) {
					throw new PersonException("findAllIdsBy ---> null");
				}
				return ids;
			}
		});
	}

	/**
	 * Invalida las caches correctas en caso de cambio de datos.
	 * @param p
	 */
	private void invalidateCaches(Person p) {

		String id = p.getId();
		if (id != null) {
			personCache.invalidate(id);
		}
		
		dnisCache.invalidateAll();
		personTypesCache.invalidateAll();
	}
	
	
	/*
	@Override
	public Person findByPrincipal(DCSysPrincipal principal) throws PersonException {
		String personId = principal.getUuid();
		Person person = findById(personId);
		return person;
	};
	*/
	

	@Override
	public List<Person> findAll() throws PersonException {
		List<String> allIds = personDAO.findAllIds();
		if (allIds == null || allIds.size() <= 0) {
			return new ArrayList<Person>();
		}
		
		try {
			return personCache.getAll(allIds).values().asList();
		} catch (ExecutionException e) {
			throw new PersonException(e.getCause().getMessage());
		}
	}

	/**
	 * Encuentra todas las peronas que tengan algun tipo de os pasados como parametro.
	 * en el caso de que types == null o types.size == 0 busca todas las peronsas que NO tengna ningun tipo.
	 */
	@Override
	public List<Person> findAllBy(List<PersonType> types) throws PersonException {
		
		
		Set<String> ids = new HashSet<String>();

		try {

			if (types == null || types.size() <= 0) {
				// personas con ningun tipo.
				ids.addAll(personDAO.findAllIdsBy(types));
			} else {
				// personas con algun tipo aunque sea.
				for (PersonType t : types) {
					List<String> i = personTypesCache.get(t);
					ids.addAll(i);
				}
			}

			List<Person> persons = new ArrayList<Person>();
			persons.addAll(personCache.getAll(ids).values());
			
			return persons;
			
		} catch (ExecutionException e) {
			if (e.getCause() instanceof PersonException) {
				throw (PersonException)e.getCause();
			} else {
				throw new PersonException(e.getCause());
			}
		}
		
	}

	
	@Override
	public Person findById(String id) throws PersonException {
		try {
			return personCache.get(id);
		} catch (ExecutionException e) {
			if (e.getCause() instanceof PersonNotFoundException) {
				return null;
			} else {
				logger.log(Level.SEVERE,e.getCause().getMessage(),e.getCause());
				throw new PersonException(e.getCause().getMessage());
			}
		}
	}
	
	@Override
	public Person findByDni(String dni) throws PersonException {
		if (dni == null) {
			throw new PersonException("dni == null");
		}
		try {
			String id = dnisCache.get(dni);
			Person person = personCache.get(id);
			return person;
		} catch (ExecutionException e) {
			if (e.getCause() instanceof PersonNotFoundException) {
				return null;
			} else {
				throw new PersonException(e.getCause());
			}
		}
	}

	@Override
	public String persist(Person p) throws PersonException {
		
		invalidateCaches(p);
		
		String id = p.getId();
		String newId = personDAO.persist(p);
		
		return newId;
	}

	@Override
	public void remove(Person p) throws PersonException {
		throw new PersonException("No implementado");
	}

	@Override
	public List<PersonType> findAllTypes() throws PersonException {
		return personDAO.findAllTypes();
	}
	
	/*
	@Override
	public Person findByStudentNumber(String studentNumber) throws PersonException {
		if (studentNumber == null) {
			return null;
		}
		
		for (Person p : findAll()) {
			if (p.getStudentNumber() != null && studentNumber.equals(p.getStudentNumber())) {
				return p;
			}  			
		}
		return null;
	}
	*/
	
}
