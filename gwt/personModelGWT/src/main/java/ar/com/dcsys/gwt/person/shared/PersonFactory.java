package ar.com.dcsys.gwt.person.shared;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.Telephone;
import ar.com.dcsys.gwt.person.shared.lists.MailList;
import ar.com.dcsys.gwt.person.shared.lists.PersonList;
import ar.com.dcsys.gwt.person.shared.lists.PersonTypeList;
import ar.com.dcsys.gwt.person.shared.lists.PersonValueList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface PersonFactory extends AutoBeanFactory {
	
	public AutoBean<Person> person();
	public AutoBean<Person> person(Person p);
	public AutoBean<Telephone> telephone();
	public AutoBean<Mail> mail();

	public AutoBean<PersonValueProxy> personValue();
	
	public AutoBean<PersonTypeList> personTypeList();
	public AutoBean<PersonList> personList();
	public AutoBean<MailList> mailList();
	public AutoBean<PersonValueList> personValueList();
	
}
