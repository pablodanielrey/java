package ar.com.dcsys.gwt.person.shared;

import ar.com.dcsys.data.person.Person;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface PersonFactory extends AutoBeanFactory {
	
	AutoBean<Person> person();
	AutoBean<Person> person(Person p);
	AutoBean<PersonList> personList();
	
}
