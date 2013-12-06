package ar.com.dcsys.model.assignment;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ar.com.dcsys.data.assignment.Assignment;
import ar.com.dcsys.data.assignment.AssignmentBean;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonBean;
import ar.com.dcsys.data.person.PersonDAO;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.data.silabouse.CourseBean;
import ar.com.dcsys.data.silabouse.Subject;
import ar.com.dcsys.data.silabouse.SubjectBean;
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
		AssignmentsManager assignmentsManager = getAssignmentsManager();
		SubjectsManager subjectsManager = getSubjectsManager();
		CoursesManager coursesManager = getCoursesManager();
        PersonDAO personDAO = getPersonDAO();
		
		//creo la persona
        String dni = "123456";
        
        Person person = new PersonBean();
        person.setName("Emanuel Joaquin");
        person.setLastName("Pais");
        person.setDni(dni);
        String id = personDAO.persist(person);
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
		assignment.setCourse(course);
		assignment.setPerson(person);
		
		id = assignmentsManager.persist(assignment);
		assertNotNull(id);
		assertNotNull(assignment.getId());
		assertEquals(assignment.getId(),id);
		
		Assignment assignment2 = assignmentsManager.findById(id);
		assertEquals(assignment.getId(), assignment2.getId());
		assertNotNull(assignment2.getCourse());		
			

		/////////////////// pruebo que las asignaciones en la base tienen todas diferentes ids ////////////////
		
		assertNotNull(person);
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
		}		
	}

}
