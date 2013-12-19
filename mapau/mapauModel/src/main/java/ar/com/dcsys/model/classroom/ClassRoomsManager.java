package ar.com.dcsys.model.classroom;

import java.util.List;

import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.exceptions.MapauException;

public interface ClassRoomsManager {
	public ClassRoom findById(String id) throws MapauException;
	public List<ClassRoom> findAll() throws MapauException;
	public ClassRoom findByName(String name) throws MapauException;

	public String persist(ClassRoom object) throws MapauException;
	public void remove(ClassRoom object) throws MapauException;
	
	public void persist(CharacteristicQuantity chars, ClassRoom classRoom) throws MapauException;
	public void remove(CharacteristicQuantity chars, ClassRoom classRoom) throws MapauException;
	public void removeAllCharacteristics(ClassRoom classRoom) throws MapauException;
}
