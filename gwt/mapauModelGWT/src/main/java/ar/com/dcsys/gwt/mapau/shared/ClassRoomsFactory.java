package ar.com.dcsys.gwt.mapau.shared;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.gwt.mapau.shared.list.CharacteristicList;
import ar.com.dcsys.gwt.mapau.shared.list.ClassRoomList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface ClassRoomsFactory extends AutoBeanFactory {

	public AutoBean<ClassRoom> classRoom();
	public AutoBean<Characteristic> characteristic();
	public AutoBean<CharacteristicQuantity> characteristicQuantity();
	
	public AutoBean<ClassRoomList> classRoomList();
	public AutoBean<CharacteristicList> characteristicList();
	
	
}
