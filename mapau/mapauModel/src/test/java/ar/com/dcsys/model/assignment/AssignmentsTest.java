package ar.com.dcsys.model.assignment;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ar.com.dcsys.data.person.PersonDAO;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.silabouse.CoursesManager;
import ar.com.dcsys.model.silabouse.SubjectsManager;

public class AssignmentsTest {
	
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
	
	private AssignmentsManager getAssignmentsManager() {
		AssignmentsManager assignmentsManager = container.instance().select(AssignmentsManager.class).get();
		return assignmentsManager;
	}
	
	private SubjectsManager getSubjectsManager() {
		SubjectsManager subjectsManager = container.instance().select(SubjectsManager.class).get();
		return subjectsManager;
	}
	
	private CoursesManager getCoursesManager() {
		CoursesManager coursesManager = container.instance().select(CoursesManager.class).get();
		return coursesManager;
	}
    
    private PersonDAO getPersonDAO() {
            PersonDAO personDAO = container.instance().select(PersonDAO.class).get();
            return personDAO;
    }
    
	

	@Test
	public void persistTest() throws MapauException, PersonException {
		//AssignmentsManager assignmentsManager = getAssignmentsManager();
		SubjectsManager subjectsManager = getSubjectsManager();
		//CoursesManager coursesManager = getCoursesManager();
        PersonDAO personDAO = getPersonDAO();
	/*	
		//creo la persona
        String dni = "123456";
        
        Person person = new PersonBean();
        person.setName("Emanuel Joaquin");
        person.setLastName("Pais");
        person.setDni(dni);
        String id = personsManager.persist(person);
        assertNotNull(id);	
		
		////// pruebo que el persist agregue una asignacion en la base ///////////////
		
		List<Assignment> assignments = assignmentsManager.findBy(person);
		assertNotNull(assignments);
		
		int count = assignments.size();
		
		Date from = new Date();
		
		// Creo la materia
		Subject subject = new SubjectBean();
		subject.setName("Mat1");
		id = subjectsManager.persist(subject);
		assertNotNull(id);		
		
		
		// Creo el Course
		Course course = new CourseBean();
		course.setName("Catedra A");
		course.setSubject(subject);
		id = coursesManager.persist(course);
		assertNotNull(id);		
		

		
		
		Assignment assignment = new AssignmentBean();
		assignment.setFrom(from);
		assignment.setAssignableUnit(course);
		assignment.setPerson(person);
		
		id = assignmentsManager.persist(assignment);
		assertNotNull(id);
		assertNotNull(assignment.getId());
		assertEquals(assignment.getId(),id);
		
		Assignment assignment2 = assignmentsManager.findById(id);
		assertEquals(assignment.getId(), assignment2.getId());
		assertNotNull(assignment2.getAssignableUnit());		
			

		/////////////////// pruebo que las aulas en la base tienen todas diferentes ids ////////////////
				
		assignments = assignmentsManager.findBy(person);
		assertEquals(assignments.size(), count + 1);
		
		for (Assignment a : assignments) {
			String ida = a.getId();
			int equals = 0;
			for (Assignment a2 : assignments) {
				assertNotNull(a2.getId());
				if (ida.equals(a2.getId())) {
					equals++;
				}
			}
			assertEquals(equals,1);
		}	*/	
	}

}
