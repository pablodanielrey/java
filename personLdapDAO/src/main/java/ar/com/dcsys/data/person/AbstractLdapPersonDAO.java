package ar.com.dcsys.data.person;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.person.types.External;
import ar.com.dcsys.data.person.types.PersonType;
import ar.com.dcsys.data.person.types.Personal;
import ar.com.dcsys.data.person.types.PostGraduate;
import ar.com.dcsys.data.person.types.Student;
import ar.com.dcsys.data.person.types.Teacher;
import ar.com.dcsys.exceptions.PersonException;

public abstract class AbstractLdapPersonDAO extends AbstractPersonDAO {

	private static final long serialVersionUID = 1L;

	protected List<PersonType> fromObjectClass(List<String> sTypes) throws PersonException {
		List<PersonType> types = new ArrayList<PersonType>();
		for (String s : sTypes) {
			if (s.contains("x-dcsys-docente")) {
				types.add(new Teacher());
			}
			if (s.contains("x-dcsys-estudiante")) {
				types.add(new Student());
			}
			if (s.contains("x-dcsys-no-docente")) {
				types.add(new Personal());
			}
			if (s.contains("x-dcsys-visitante")) {
				types.add(new External());
			}
			if (s.contains("x-dcsys-posgrado")) {
				types.add(new PostGraduate());
			}
		}
		return types;
	}
	
	protected List<String> toObjectClass(List<PersonType> types) throws PersonException {
		List<String> t = new ArrayList<>();
		for (PersonType pt : types) {
			if (pt instanceof External) {
				t.add("x-dcsys-visitante");
				continue;
			}
			if (pt instanceof Teacher) {
				t.add("x-dcsys-docente");
				continue;
			}
			if (pt instanceof Student) {
				t.add("x-dcsys-estudiante");
				continue;
			}
			if (pt instanceof Personal) {
				t.add("x-dcsys-no-docente");
				continue;
			}
			if (pt instanceof PostGraduate) {
				t.add("x-dcsys-posgrado");
			}
		}	
		return t;
	}
		
	
}
