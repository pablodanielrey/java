package ar.com.dcsys.gwt.mapau.server;

import javax.inject.Inject;

import ar.com.dcsys.data.silabouse.CourseDAO;
import ar.com.dcsys.data.silabouse.Subject;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.model.silabouse.SubjectsManager;

public class CourseDAOParams implements CourseDAO.Params {

	private final SubjectsManager subjectsManager;
	
	@Inject
	public CourseDAOParams(SubjectsManager subjectsManager) {
		this.subjectsManager = subjectsManager;
	}
	
	@Override
	public Subject findSubjectById(String id) throws MapauException {
		return subjectsManager.findById(id);
	}

}
