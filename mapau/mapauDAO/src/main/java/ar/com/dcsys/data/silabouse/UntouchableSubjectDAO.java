package ar.com.dcsys.data.silabouse;

import java.io.Serializable;
import java.util.List;

import ar.com.dcsys.exceptions.MapauException;

public interface UntouchableSubjectDAO extends Serializable {

	public String persist(UntouchableSubject untouchableSubjects) throws MapauException;
	public void remove(UntouchableSubject untouchableSubjects) throws MapauException;
	
	public UntouchableSubject findById(String id) throws MapauException;
	public List<UntouchableSubject> findAll() throws MapauException;
	public List<String> findAllIds() throws MapauException;
	public String findIdByArea(String id) throws MapauException;
	
	public void initialize() throws MapauException;
	
	public interface Params {
		public Course findCourseById(String id) throws MapauException;
		public Area findAreaById(String id) throws MapauException;
	}
}
