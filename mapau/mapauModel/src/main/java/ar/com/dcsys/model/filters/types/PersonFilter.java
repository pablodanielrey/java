package ar.com.dcsys.model.filters.types;
import java.util.Comparator;
import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.exceptions.FilterException;
import ar.com.dcsys.model.filters.PropertyAccesor;
import ar.com.dcsys.model.filters.PropertyFilter;

public class PersonFilter extends PropertyFilter<Person> {

	private static final Comparator<Person> p = new Comparator<Person>() {

		@Override
		public int compare(Person o1, Person o2) {
			if (o1 == null && o2 == null) {
				return 0;
			}
			
			if (o1 == null) {
				return -1;
			}
			
			if (o2 == null) {
				return 1;
			}
			
			if (o1.getId() == null && o2.getId() == null) {
				return 0;
			}
			
			if (o1.getId() == null) {
				return -1;
			}
			
			if (o2.getId() == null) {
				return 1;
			}
			
			return o1.getId().compareTo(o2.getId());
		}
		
	};
	
	private static final PropertyAccesor<Person> pc = new PropertyAccesor<Person>() {
		@Override
		public boolean multipleValues() {
			return true;
		}

		@Override
		public List<Person> getProperties(ReserveAttemptDate ra) throws FilterException {
			if (ra.getCourse() == null) {
				throw new FilterException();
			}
			return ra.getRelatedPersons();
		}
		
		@Override
		public Person getProperty(ReserveAttemptDate ra)	throws FilterException {
			throw new FilterException("multiple values posibles");
		}
	};
	
	public PersonFilter(Person person) throws FilterException {
		super(person, p, pc);
	}

}
