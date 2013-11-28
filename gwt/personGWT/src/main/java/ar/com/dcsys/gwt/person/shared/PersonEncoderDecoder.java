package ar.com.dcsys.gwt.person.shared;

import java.util.List;

import javax.inject.Inject;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

public class PersonEncoderDecoder {

	private final PersonFactory personFactory;
	
	@Inject
	public PersonEncoderDecoder(PersonFactory personFactory) {
		this.personFactory = personFactory;
		
	}
	
	public String encode(PersonProxy person) {
		AutoBean<PersonProxy> bean = AutoBeanUtils.getAutoBean(person);
		String json = AutoBeanCodex.encode(bean).getPayload();
		return json;
	}
	
	public PersonProxy decode(String json) {
		AutoBean<PersonProxy> bean = AutoBeanCodex.decode(personFactory, PersonProxy.class, json);
		PersonProxy person = bean.as();
		return person;
	}	
	
	
	public String encodeList(List<PersonProxy> list) {
		AutoBean<PersonList> bean = personFactory.personList();
		PersonList personList = bean.as();
		personList.setPersons(list);
		
		String json = AutoBeanCodex.encode(bean).getPayload();
		return json;
	}
	
	public List<PersonProxy> decodeList(String list) {
		AutoBean<PersonList> bean = AutoBeanCodex.decode(personFactory, PersonList.class, list);
		PersonList personList = bean.as();
		List<PersonProxy> persons = personList.getPersons();
		return persons;
	}
}
