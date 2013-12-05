package ar.com.dcsys.data.classroom;

import java.io.Serializable;
import java.util.List;

import ar.com.dcsys.exceptions.MapauException;

public interface CharacteristicDAO extends Serializable {

	public String persist(Characteristic object) throws MapauException;
	public void remove(Characteristic object) throws MapauException;	
	public Characteristic findById(String id) throws MapauException;
	public Characteristic findByName(String string) throws MapauException;
	public List<Characteristic> findAll() throws MapauException;	
	public List<Characteristic> findAll(Boolean historic) throws MapauException;
	public List<String> findAllIds() throws MapauException ;
	public String findIdByName(String name) throws MapauException;
		
}
