package ar.com.dcsys.model.silabouse;

import javax.inject.Inject;

import ar.com.dcsys.data.silabouse.CourseDAO;
import ar.com.dcsys.data.silabouse.Subject;
import ar.com.dcsys.exceptions.MapauException;

public class CourseParams implements CourseDAO.Params {

	private final SubjectsManager subjectManager;
	
	@Inject
	public CourseParams(SubjectsManager subjectManager) {
		this.subjectManager = subjectManager;
	}
	@Override
	public Subject findSubjectById(String id) throws MapauException {
		return subjectManager.findById(id);
	}

}
