package ar.com.dcsys.model;

import java.util.List;

import static org.junit.Assert.*;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.PersonException;

public class PersonsManagerTest {

	private static Weld weld;
	private static WeldContainer container;
	
	
	@BeforeClass
	public static void setEnvironment() {
		weld = new Weld();
		container = weld.initialize();
	}

	
	@AfterClass
	public static void destroyEnvironment() {
		if (weld != null) {
			weld.shutdown();
		}
	}

	
	private PersonsManager getPersonsManager() {
		PersonsManager personsManager = container.instance().select(PersonsManager.class).get();
		return personsManager;
	}
	
	
	@Test
	public void persistTest() throws PersonException {
		PersonsManager personsManager = getPersonsManager();

		
		///// pruebo que el persist agregue una persona en la base //////////////
		
		List<Person> persons = personsManager.findAll();
		int count = persons.size();
		
		String dni = "123456";
		
		Person person = new Person();
		person.setName("Pablo Daniel");
		person.setLastName("Rey");
		person.setDni(dni);
		String id = personsManager.persist(person);
		assertNotNull(id);
		
		Person person3 = personsManager.findById(id);
		assertEquals(person.getId(), person3.getId());
		assertNotNull(person3.getName());
		assertNotNull(person3.getLastName());
		assertNotNull(person3.getDni());

		/////////////////// pruebo que las personas en la base tienen todas diferentes ids ////////////////
				
		persons = personsManager.findAll();
		assertEquals(persons.size(), count + 1);
		
		for (Person p : persons) {
			String idp = p.getId();
			int equals = 0;
			for (Person p2 : persons) {
				assertNotNull(p2.getId());
				if (idp.equals(p2.getId())) {
					equals++;
				}
			}
			assertEquals(equals,1);
		}
		
		
	}
	
	@Test
	public void findAllTest() throws PersonException {
		PersonsManager personsManager = getPersonsManager();
		List<Person> persons = personsManager.findAll();
		assertNotNull(persons);
		
		for (Person p : persons) {
			assertNotNull(p.getId());
		}
	}

}
