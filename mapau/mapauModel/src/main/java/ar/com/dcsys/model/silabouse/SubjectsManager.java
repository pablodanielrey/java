package ar.com.dcsys.model.silabouse;

import java.util.List;

import ar.com.dcsys.data.silabouse.Subject;
import ar.com.dcsys.exceptions.MapauException;

public interface SubjectsManager {

	public String persist(Subject subject) throws MapauException;
	public List<Subject> findAll() throws MapauException;
	public Subject findById(String id) throws MapauException;
	
}
