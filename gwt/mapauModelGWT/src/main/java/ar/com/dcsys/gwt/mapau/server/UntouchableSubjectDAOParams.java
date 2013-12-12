package ar.com.dcsys.gwt.mapau.server;

import javax.inject.Inject;

import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.data.silabouse.UntouchableSubjectDAO;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.model.silabouse.AreasManager;
import ar.com.dcsys.model.silabouse.CoursesManager;

public class UntouchableSubjectDAOParams implements UntouchableSubjectDAO.Params {

	private final CoursesManager coursesManager;
	private final AreasManager areasManager;
	
	@Inject
	public UntouchableSubjectDAOParams(CoursesManager coursesManager, AreasManager areasManager) {
		this.coursesManager = coursesManager;
		this.areasManager = areasManager;
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
