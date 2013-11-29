package ar.com.dcsys.gwt.person.server.inject;

import javax.enterprise.inject.Produces;

import ar.com.dcsys.gwt.person.shared.PersonFactory;

import com.google.web.bindery.autobean.vm.AutoBeanFactorySource;

public class PersonFactoryProducer {

	@Produces
	public PersonFactory personFactory() {
		PersonFactory personFactory = AutoBeanFactorySource.create(PersonFactory.class);
		return personFactory;
	}
	
}
