package ar.com.dcsys.data.classroom;

import java.io.Serializable;
import java.util.List;

import ar.com.dcsys.exceptions.MapauException;

public interface ClassRoomDAO extends Serializable {

	public String persist(ClassRoom object) throws MapauException ;
	public void remove(ClassRoom object) throws MapauException ;
	
	public ClassRoom findById(String id) throws MapauException ;
	public ClassRoom findByName(String name) throws MapauException ;
	public List<ClassRoom> findAll() throws MapauException ;
	
	public List<String> findAllIds() throws MapauException;
	public String findIdByName(String name) throws MapauException;
	
	public void persist(CharacteristicQuantity chars, ClassRoom classRoom) throws MapauException;
	public void remove(CharacteristicQuantity chars, ClassRoom classRoom) throws MapauException;
	public void removeAllCharacteristics(ClassRoom classRoom) throws MapauException;
	
	public interface Params {
		public Characteristic findCharacteristicById(String id) throws MapauException;
	}
	
}
