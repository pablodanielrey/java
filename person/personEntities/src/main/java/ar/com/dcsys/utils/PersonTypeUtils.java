package ar.com.dcsys.utils;

import java.util.List;

import ar.com.dcsys.data.person.PersonType;

public class PersonTypeUtils {

	public static String getDescription(PersonType type) {
		switch (type) {
			case EXTERNAL: return "Externo";
			case PERSONAL: return "Empleado";
			case POSTGRADUATE: return "Posgrado";
			case STUDENT: return "Estudiante";
			case TEACHER: return "Docente";
		}
		return "";
	}
	
	public List<PersonType> filter(List<PersonType> types) {
		return types;
	}
	
}
