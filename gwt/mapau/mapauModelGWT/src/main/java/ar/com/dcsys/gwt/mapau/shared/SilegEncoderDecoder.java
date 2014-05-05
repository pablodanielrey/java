package ar.com.dcsys.gwt.mapau.shared;

import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.gwt.autoBeans.shared.AutoBeanUtils;
import ar.com.dcsys.gwt.mapau.shared.list.CourseList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class SilegEncoderDecoder {

	private final SilegFactory silegFactory;
	
	@Inject
	public SilegEncoderDecoder(SilegFactory silegFactory) {
		this.silegFactory = silegFactory;
	}
	
	public List<Course> decodeCourseList(String list) {
		AutoBean<CourseList> bean = AutoBeanCodex.decode(silegFactory, CourseList.class, list);
		CourseList l = bean.as();
		List<Course> values = l.getList();
		return values;
	}
	
	public String encodeCourseList(List<Course> list) {
		AutoBean<CourseList> al = silegFactory.courseList();
		CourseList l = al.as();
		
		List<Course> cl = AutoBeanUtils.wrapList(silegFactory, Course.class, list);
		l.setList(cl);
		
		String json = AutoBeanCodex.encode(al).getPayload();
		return json;
	}	
	
}
