package ar.com.dcsys.data.person;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractPersonDAO implements PersonDAO {

	private static final long serialVersionUID = 1L;

	@Override
	public List<PersonType> findAllTypes() {
		return Arrays.asList(PersonType.values());
	}	
	
}
