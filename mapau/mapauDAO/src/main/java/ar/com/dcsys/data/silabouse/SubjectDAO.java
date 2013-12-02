package ar.com.dcsys.data.silabouse;

import java.io.Serializable;
import java.util.List;

import ar.com.dcsys.exceptions.MapauException;

public interface SubjectDAO  extends Serializable {

	public String persist(Subject object) throws MapauException ;
	public List<Subject> findAll() throws MapauException ;
	public Subject findById(String id) throws MapauException ;
	
	public void initialize() throws MapauException;
}
