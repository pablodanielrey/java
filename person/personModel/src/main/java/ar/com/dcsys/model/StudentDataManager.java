package ar.com.dcsys.model;

import ar.com.dcsys.data.person.StudentData;
import ar.com.dcsys.exceptions.PersonException;

public interface StudentDataManager {
	public String fingIdByStudentNumber(String sn) throws PersonException;
	public StudentData findByStudentNumber(String sn) throws PersonException;
}
