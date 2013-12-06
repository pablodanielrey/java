package ar.com.dcsys.gwt.mapau.shared;

import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.gwt.autoBeans.shared.AutoBeanUtils;
import ar.com.dcsys.gwt.mapau.shared.list.CourseList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class SilegEncoderDecoder {

	private final MapauFactory mapauFactory;
	
	@Inject
	public SilegEncoderDecoder(MapauFactory mapauFactory) {
		this.mapauFactory = mapauFactory;
	}

	
	/**
	 * Codifcación genérica de una clase a String usando AutoBeanCodex
	 * @param clazz
	 * @param t
	 * @return
	 */
	public <T> String encode(Class<T> clazz, T t) {
		return AutoBeanUtils.encode(mapauFactory, clazz, t);
	}
	
	/**
	 * Decodificiacion genérica de una clase desde un String usando AutoBeanCodex.
	 * @param clazz
	 * @param json
	 * @return
	 */
	public <T> T decode(Class<T> clazz, String json) {
		return AutoBeanUtils.decode(mapauFactory, clazz, json);
	}			
	
	
	
	
	public List<Course> decodeCourseList(String list) {
		AutoBean<CourseList> bean = AutoBeanCodex.decode(mapauFactory, CourseList.class, list);
		CourseList l = bean.as();
		List<Course> values = l.getList();
		return values;
	}
	
	public String encodeCourseList(List<Course> list) {
		AutoBean<CourseList> al = mapauFactory.courseList();
		CourseList l = al.as();
		
		List<Course> cl = AutoBeanUtils.wrapList(mapauFactory, Course.class, list);
		l.setList(cl);
		
		String json = AutoBeanCodex.encode(al).getPayload();
		return json;
	}	
	
}
