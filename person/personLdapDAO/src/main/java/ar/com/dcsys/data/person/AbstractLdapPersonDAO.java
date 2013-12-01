package ar.com.dcsys.data.person;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.exceptions.PersonException;

public abstract class AbstractLdapPersonDAO extends AbstractPersonDAO {

	private static final long serialVersionUID = 1L;

	protected List<PersonType> fromObjectClass(List<String> sTypes) throws PersonException {
		List<PersonType> types = new ArrayList<PersonType>();
		for (String s : sTypes) {
			if (s.contains("x-dcsys-docente")) {
				types.add(PersonType.TEACHER);
			}
			if (s.contains("x-dcsys-estudiante")) {
				types.add(PersonType.STUDENT);
			}
			if (s.contains("x-dcsys-no-docente")) {
				types.add(PersonType.PERSONAL);
			}
			if (s.contains("x-dcsys-visitante")) {
				types.add(PersonType.EXTERNAL);
			}
			if (s.contains("x-dcsys-posgrado")) {
				types.add(PersonType.POSTGRADUATE);
			}
		}
		return types;
	}
	
	protected List<String> toObjectClass(List<PersonType> types) throws PersonException {
		List<String> t = new ArrayList<>();
		for (PersonType pt : types) {
			if (pt.equals(PersonType.EXTERNAL)) {
				t.add("x-dcsys-visitante");
				continue;
			}
			if (pt.equals(PersonType.TEACHER)) {
				t.add("x-dcsys-docente");
				continue;
			}
			if (pt.equals(PersonType.STUDENT)) {
				t.add("x-dcsys-estudiante");
				continue;
			}
			if (pt.equals(PersonType.PERSONAL)) {
				t.add("x-dcsys-no-docente");
				continue;
			}
			if (pt.equals(PersonType.POSTGRADUATE)) {
				t.add("x-dcsys-posgrado");
			}
		}	
		return t;
	}
		
	
}
