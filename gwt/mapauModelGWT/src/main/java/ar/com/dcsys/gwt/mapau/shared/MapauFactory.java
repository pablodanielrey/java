package ar.com.dcsys.gwt.mapau.shared;

import ar.com.dcsys.data.filter.TransferFilter;
import ar.com.dcsys.gwt.mapau.shared.list.ClassRoomList;
import ar.com.dcsys.gwt.mapau.shared.list.CourseList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface MapauFactory extends AutoBeanFactory {

	public AutoBean<TransferFilter> transferFilter();
	
	public AutoBean<ClassRoomList> classRoomList();
	public AutoBean<CourseList> courseList();
	
	
}
