package ar.com.dcsys.gwt.person.shared;

import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.gwt.autoBeans.shared.AutoBeanUtils;
import ar.com.dcsys.gwt.person.shared.lists.MailList;
import ar.com.dcsys.gwt.person.shared.lists.PersonList;
import ar.com.dcsys.gwt.person.shared.lists.PersonTypeList;
import ar.com.dcsys.gwt.person.shared.lists.PersonValueList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;


public class PersonEncoderDecoder {

	private final PersonFactory personFactory;
	
	@Inject
	public PersonEncoderDecoder(PersonFactory personFactory) {
		this.personFactory = personFactory;
	}

	
	/**
	 * Codifcación genérica de una clase a String usando AutoBeanCodex
	 * @param clazz
	 * @param t
	 * @return
	 */
	public <T> String encode(Class<T> clazz, T t) {
		return AutoBeanUtils.encode(personFactory, clazz, t);
	}
	
	/**
	 * Decodificiacion genérica de una clase desde un String usando AutoBeanCodex.
	 * @param clazz
	 * @param json
	 * @return
	 */
	public <T> T decode(Class<T> clazz, String json) {
		return AutoBeanUtils.decode(personFactory, clazz, json);
	}			
	
	
	public String encodePersonValueList(List<PersonValueProxy> pt) {
		AutoBean<PersonValueList> aptl = personFactory.personValueList();
		PersonValueList ptl = aptl.as();
		
		List<PersonValueProxy> wpt = AutoBeanUtils.wrapList(personFactory, PersonValueProxy.class, pt);
		ptl.setList(wpt);
		
		String json = AutoBeanCodex.encode(aptl).getPayload();
		return json;
	}
	

	public List<PersonValueProxy> decodePersonValueList(String list) {
		AutoBean<PersonValueList> bean = AutoBeanCodex.decode(personFactory, PersonValueList.class, list);
		PersonValueList l = bean.as();
		List<PersonValueProxy> values = l.getList();
		return values;
	}	
	
	
	public String encodeTypeList(List<PersonType> pt) {
		AutoBean<PersonTypeList> aptl = personFactory.personTypeList();
		PersonTypeList ptl = aptl.as();
		ptl.setList(pt);

		// no es necesario hacerle wrapping porque los elementos son enumerativos.
		
		String json = AutoBeanCodex.encode(aptl).getPayload();
		return json;
	}
	

	public List<PersonType> decodeTypeList(String list) {
		AutoBean<PersonTypeList> bean = AutoBeanCodex.decode(personFactory, PersonTypeList.class, list);
		PersonTypeList l = bean.as();
		List<PersonType> types = l.getList();
		return types;
	}
	
	
	public String encodeMailList(List<Mail> pt) {
		AutoBean<MailList> aptl = personFactory.mailList();
		MailList ptl = aptl.as();
		
		List<Mail> wpt = AutoBeanUtils.wrapList(personFactory, Mail.class, pt);
		ptl.setList(wpt);
		
		String json = AutoBeanCodex.encode(aptl).getPayload();
		return json;
	}
	
	public List<Mail> decodeMailList(String list) {
		AutoBean<MailList> bean = AutoBeanCodex.decode(personFactory, MailList.class, list);
		MailList mailList = bean.as();
		List<Mail> mails = mailList.getList();
		return mails;
	}
	
	
	public String encodePersonList(List<Person> pt) {
		AutoBean<PersonList> aptl = personFactory.personList();
		PersonList ptl = aptl.as();
		
		List<Person> wpt = AutoBeanUtils.wrapList(personFactory, Person.class, pt);
		ptl.setList(wpt);
		
		String json = AutoBeanCodex.encode(aptl).getPayload();
		return json;
	}
	
	public List<Person> decodePersonList(String list) {
		AutoBean<PersonList> bean = AutoBeanCodex.decode(personFactory, PersonList.class, list);
		PersonList personList = bean.as();
		List<Person> persons = personList.getList();
		return persons;
	}
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
}
