package ar.com.dcsys.data.person;

import java.util.List;

import ar.com.dcsys.exceptions.PersonException;

public interface StudentDataDAO {

	public String persist(StudentData sd) throws PersonException;
	public StudentData findById(String id) throws PersonException;
	public List<StudentData> findAll() throws PersonException;
	
	public StudentData findByStudentNumber(String sn) throws PersonException;
	
}
