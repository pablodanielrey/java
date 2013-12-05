package ar.com.dcsys.model.silabouse;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.silabouse.Subject;
import ar.com.dcsys.data.silabouse.SubjectDAO;
import ar.com.dcsys.exceptions.MapauException;

@Singleton
public class SubjectsManagerBean implements SubjectsManager {
	
	private static Logger logger = Logger.getLogger(SubjectsManager.class.getName());

	private final SubjectDAO subjectDAO;
	
	@Inject
	public SubjectsManagerBean(SubjectDAO subjectDAO) {
		this.subjectDAO = subjectDAO;
		createCaches();
	}
	
	private void createCaches() {
		
	}

	@Override
	public List<Subject> findAll() throws MapauException {
		return subjectDAO.findAll();
	}

	@Override
	public Subject findById(String id) throws MapauException {
		return subjectDAO.findById(id);
	}

	@Override
	public String persist(Subject subject) throws MapauException {
		return subjectDAO.persist(subject);
	}
	
}
