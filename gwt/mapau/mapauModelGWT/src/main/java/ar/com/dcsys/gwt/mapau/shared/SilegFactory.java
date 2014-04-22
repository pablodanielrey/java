package ar.com.dcsys.gwt.mapau.shared;

import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.gwt.mapau.shared.list.CourseList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface SilegFactory extends AutoBeanFactory {

	public AutoBean<Course> course();
	public AutoBean<CourseList> courseList();
	
	
}
