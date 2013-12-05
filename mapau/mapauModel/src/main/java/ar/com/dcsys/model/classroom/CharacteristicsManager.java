package ar.com.dcsys.model.classroom;

import java.util.List;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.exceptions.MapauException;

public interface CharacteristicsManager {
	public Characteristic findById(String id) throws MapauException;
	public List<Characteristic> findAll() throws MapauException;
	public Characteristic findByName(String name) throws MapauException;
	
	public String persist(Characteristic object) throws MapauException;
	public void remove(Characteristic object) throws MapauException;
}
