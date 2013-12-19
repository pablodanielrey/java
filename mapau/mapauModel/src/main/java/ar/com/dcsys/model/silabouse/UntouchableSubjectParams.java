package ar.com.dcsys.model.silabouse;

import javax.inject.Inject;

import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.data.silabouse.UntouchableSubjectDAO;
import ar.com.dcsys.exceptions.MapauException;

public class UntouchableSubjectParams implements UntouchableSubjectDAO.Params {

	private final CoursesManager coursesManager;
	private final AreasManager areasManager;
	
	
	@Inject
	public UntouchableSubjectParams(CoursesManager coursesManager, AreasManager areasManager) {
		this.areasManager = areasManager;
		this.coursesManager = coursesManager;
	}
	
	@Override
	public Course findCourseById(String id) throws MapauException {
		return coursesManager.findById(id);
	}

	@Override
	public Area findAreaById(String id) throws MapauException {
		return areasManager.findById(id);
	}

}
