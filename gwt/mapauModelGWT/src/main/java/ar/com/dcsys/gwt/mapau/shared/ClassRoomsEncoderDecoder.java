package ar.com.dcsys.gwt.mapau.shared;

import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.gwt.autoBeans.shared.AutoBeanUtils;
import ar.com.dcsys.gwt.mapau.shared.list.CharacteristicList;
import ar.com.dcsys.gwt.mapau.shared.list.ClassRoomList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class ClassRoomsEncoderDecoder {

	private final ClassRoomsFactory classRoomsFactory;
	
	@Inject
	public ClassRoomsEncoderDecoder(ClassRoomsFactory classRoomsFactory) {
		this.classRoomsFactory = classRoomsFactory;
	}
	
	public List<ClassRoom> decodeClassRoomList(String list) {
		AutoBean<ClassRoomList> bean = AutoBeanCodex.decode(classRoomsFactory, ClassRoomList.class, list);
		ClassRoomList l = bean.as();
		List<ClassRoom> values = l.getList();
		return values;
	}
	
	public String encodeClassRoomList(List<ClassRoom> list) {
		AutoBean<ClassRoomList> al = classRoomsFactory.classRoomList();
		ClassRoomList l = al.as();
		
		List<ClassRoom> cl = AutoBeanUtils.wrapList(classRoomsFactory, ClassRoom.class, list);
		l.setList(cl);
		
		String json = AutoBeanCodex.encode(al).getPayload();
		return json;
	}
	
	

	public List<Characteristic> decodeCharacteristicList(String list) {
		AutoBean<CharacteristicList> bean = AutoBeanCodex.decode(classRoomsFactory, CharacteristicList.class, list);
		CharacteristicList l = bean.as();
		List<Characteristic> values = l.getList();
		return values;
	}
	
	public String encodeCharacteristicList(List<Characteristic> list) {
		AutoBean<CharacteristicList> al = classRoomsFactory.characteristicList();
		CharacteristicList l = al.as();
		
		List<Characteristic> cl = AutoBeanUtils.wrapList(classRoomsFactory, Characteristic.class, list);
		l.setList(cl);
		
		String json = AutoBeanCodex.encode(al).getPayload();
		return json;
	}
	
}
