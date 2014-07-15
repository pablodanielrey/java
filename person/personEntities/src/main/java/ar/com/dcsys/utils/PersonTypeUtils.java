package ar.com.dcsys.utils;

import java.util.ArrayList;
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
	
	
	/**
	 * Para filtrar en las vistas los person types que se deben ver.
	 * HORRIBLE HACK. se debe eliminar.
	 * @param types
	 * @return
	 */
	public static List<PersonType> filter(List<PersonType> types) {
		List<PersonType> pts = new ArrayList<PersonType>();
		for (PersonType pt : types) {
			// solo los tipos habilitados son : EXTERNAL y PERSONAL
			if (!((PersonType.EXTERNAL.equals(pt)) || (PersonType.PERSONAL.equals(pt)))) {
				continue;
			}
			pts.add(pt);
		}
		
		return pts;
	}
	
}
