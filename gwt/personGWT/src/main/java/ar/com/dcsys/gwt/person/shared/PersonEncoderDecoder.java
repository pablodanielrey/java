package ar.com.dcsys.gwt.person.shared;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.person.Person;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

public class PersonEncoderDecoder {

	private final PersonFactory personFactory;
	
	@Inject
	public PersonEncoderDecoder(PersonFactory personFactory) {
		this.personFactory = personFactory;
		
	}
	
	public String encode(Person person) {
		AutoBean<Person> bean = AutoBeanUtils.getAutoBean(person);
		String json = AutoBeanCodex.encode(bean).getPayload();
		return json;
	}
	
	public Person decode(String json) {
		AutoBean<Person> bean = AutoBeanCodex.decode(personFactory, Person.class, json);
		Person person = bean.as();
		return person;
	}	
	
	
	private List<Person> wrapList(List<Person> l) {
		List<Person> r = new ArrayList<Person>(l.size());
		for (Person p : l) {
			if (AutoBeanUtils.getAutoBean(p) != null) {
				r.add(p);
			} else {
				r.add(personFactory.person(p).as());
			}
		}
		return r;
	}
	
	
	public String encodeList(List<Person> list) {
		AutoBean<PersonList> bean = personFactory.personList();
		PersonList personList = bean.as();
		
		// por un error de AutoBean hay que hacerle wraping a los elementos que no tengan un AutoBean asociado.
		personList.setPersons(wrapList(list));
		
		String json = AutoBeanCodex.encode(bean).getPayload();
		return json;
	}
	
	public List<Person> decodeList(String list) {
		AutoBean<PersonList> bean = AutoBeanCodex.decode(personFactory, PersonList.class, list);
		PersonList personList = bean.as();
		List<Person> persons = personList.getPersons();
		return persons;
	}
}
