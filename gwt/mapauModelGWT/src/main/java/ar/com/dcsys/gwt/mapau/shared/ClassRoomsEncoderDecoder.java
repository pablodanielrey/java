package ar.com.dcsys.gwt.mapau.shared;

import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.gwt.autoBeans.shared.AutoBeanUtils;
import ar.com.dcsys.gwt.mapau.shared.list.ClassRoomList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class ClassRoomsEncoderDecoder {

	private final MapauFactory mapauFactory;
	
	@Inject
	public ClassRoomsEncoderDecoder(MapauFactory mapauFactory) {
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
	
	
	
	
	public List<ClassRoom> decodeClassRoomList(String list) {
		AutoBean<ClassRoomList> bean = AutoBeanCodex.decode(mapauFactory, ClassRoomList.class, list);
		ClassRoomList l = bean.as();
		List<ClassRoom> values = l.getList();
		return values;
	}
	
	public String encodeClassRoomList(List<ClassRoom> list) {
		AutoBean<ClassRoomList> al = mapauFactory.classRoomList();
		ClassRoomList l = al.as();
		
		List<ClassRoom> cl = AutoBeanUtils.wrapList(mapauFactory, ClassRoom.class, list);
		l.setList(cl);
		
		String json = AutoBeanCodex.encode(al).getPayload();
		return json;
	}
	
}
