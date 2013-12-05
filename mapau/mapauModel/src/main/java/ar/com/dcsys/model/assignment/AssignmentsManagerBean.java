package ar.com.dcsys.model.assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.assignment.Assignment;
import ar.com.dcsys.data.assignment.AssignmentDAO;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.exceptions.MapauException;

@Singleton
public class AssignmentsManagerBean implements AssignmentsManager {
	
	private static Logger logger = Logger.getLogger(AssignmentsManagerBean.class.getName());
	

	private final AssignmentDAO assignmentDAO;
	
	
	@Inject
	public AssignmentsManagerBean(AssignmentDAO assignmentDAO) {
		this.assignmentDAO = assignmentDAO;
		createCaches();
	}	
	
	private void createCaches() {
		
	}

	@Override
	public String persist(Assignment assignment) throws MapauException {
		return assignmentDAO.persist(assignment);
	}

	@Override
	public Assignment findById(String id) throws MapauException {
		return assignmentDAO.findById(id);
	}
	
	private List<Person> getPersonsOf(List<Assignment> assignments) {
		List<Person> persons = new ArrayList<>();
		for (Assignment a : assignments) {
			Person person = a.getPerson();
			if (!persons.contains(person)) {
				persons.add(a.getPerson());
			}
		}
		return persons;
	}

	/**
	 * Encuentra todas las personas que tengan asignaciones al curso determinado por el parámetro.
	 * no importa que tipo de asingacion sea.
	 */
	@Override
	public List<Person> findPersonsBy(Course course) throws MapauException {
		return findPersonsBy(course, null);
	}
	
	/**
	 * Encuentra todas las personas que tienen asignaciones de un determinado tipo en determinado curso.
	 * si el tipo == null entonces retorna todas.
	 */
	@Override
	public List<Person> findPersonsBy(Course course, String type) throws MapauException {

		List<Assignment> assignments = assignmentDAO.findBy(course);
		List<Assignment> assignmentsRet = new ArrayList<Assignment>();
		if (type == null) {
			for (Assignment a : assignments) {
				if (a.getAssignableUnit().getId().equals(course.getId())) {
					assignmentsRet.add(a);
				}
			}
		} else {
			for (Assignment a : assignments) {
				if (a.getAssignableUnit().getId().equals(course.getId()) && a.getType().equals(type)) {
					assignmentsRet.add(a);
				}
			}
		}
		List<Person> persons = getPersonsOf(assignmentsRet);
		return persons;

	}
	
	@Override
	public List<Person> getTeachersBy(Course course) throws MapauException {
		return findPersonsBy(course, "teacher");
	}


	/**
	 * Encuentra todas lasasignaciones de determinada persona.
	 */
	@Override
	public List<Assignment> findBy(Person person) throws MapauException {
		String personId = person.getId();

		List<Assignment> assignments = assignmentDAO.findBy(person);
		List<Assignment> assignmentsRet = new ArrayList<Assignment>();
		for (Assignment a : assignments) {
			String id = a.getPerson().getId();
			if (personId.equals(id)) {
				assignmentsRet.add(a);
			}
		}
		return assignmentsRet;
	
	}

	
}
