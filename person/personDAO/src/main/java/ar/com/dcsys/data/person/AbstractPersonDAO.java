package ar.com.dcsys.data.person;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.person.types.External;
import ar.com.dcsys.data.person.types.PersonType;
import ar.com.dcsys.data.person.types.Personal;
import ar.com.dcsys.data.person.types.PostGraduate;
import ar.com.dcsys.data.person.types.Student;
import ar.com.dcsys.data.person.types.Teacher;

public abstract class AbstractPersonDAO implements PersonDAO {

	private static final long serialVersionUID = 1L;

	@Override
	public List<PersonType> findAllTypes() {
		List<PersonType> types = new ArrayList<PersonType>();
		types.add(new Teacher());
		types.add(new Student());
		types.add(new Personal());
		types.add(new External());
		types.add(new PostGraduate());
		return types;
	}	
	
}
