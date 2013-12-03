package ar.com.dcsys.gwt.person.shared;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.gwt.person.shared.lists.MailList;
import ar.com.dcsys.gwt.person.shared.lists.PersonList;
import ar.com.dcsys.gwt.person.shared.lists.PersonTypeList;
import ar.com.dcsys.gwt.person.shared.lists.PersonValueList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

public class PersonEncoderDecoder {

	private final PersonFactory personFactory;
	
	@Inject
	public PersonEncoderDecoder(PersonFactory personFactory) {
		this.personFactory = personFactory;
		
	}
	
	/**
	 * Generico para hacer wrapping a una lista para que funcione la serializacion de autobeans.
	 * @param clazz
	 * @param l
	 * @return
	 */
	private <T> List<T> wrapList(Class<T> clazz, List<T> l) {
		List<T> r = new ArrayList<T>(l.size());
		for (T t : l) {
			if (AutoBeanUtils.getAutoBean(t) != null) {
				r.add(t);
			} else {
				r.add(personFactory.create(clazz).as());
			}
		}
		return r;
	}

	/**
	 * Codifcación genérica de una clase a String usando AutoBeanCodex
	 * @param clazz
	 * @param t
	 * @return
	 */
	public <T> String encode(Class<T> clazz, T t) {
		AutoBean<T> bean = AutoBeanUtils.getAutoBean(t);
		String json = AutoBeanCodex.encode(bean).getPayload();
		return json;
	}
	
	/**
	 * Decodificiacion genérica de una clase desde un String usando AutoBeanCodex.
	 * @param clazz
	 * @param json
	 * @return
	 */
	public <T> T decodePerson(Class<T> clazz, String json) {
		AutoBean<T> bean = AutoBeanCodex.decode(personFactory, clazz, json);
		T t = bean.as();
		return t;
	}		
	
	
	
	public String encodePerson(Person person) {
		AutoBean<Person> bean = AutoBeanUtils.getAutoBean(person);
		String json = AutoBeanCodex.encode(bean).getPayload();
		return json;
	}
	
	public Person decodePerson(String json) {
		AutoBean<Person> bean = AutoBeanCodex.decode(personFactory, Person.class, json);
		Person person = bean.as();
		return person;
	}		
	

	public String encodeMail(Mail m) {
		AutoBean<Mail> bean = AutoBeanUtils.getAutoBean(m);
		String json = AutoBeanCodex.encode(bean).getPayload();
		return json;
	}

	
	public Mail decodeMail(String json) {
		AutoBean<Mail> bean = AutoBeanCodex.decode(personFactory, Mail.class, json);
		Mail m = bean.as();
		return m;
	}	
	
	
	
	
	
	public String encodePersonValueList(List<PersonValueProxy> pt) {
		AutoBean<PersonValueList> aptl = personFactory.personValueList();
		PersonValueList ptl = aptl.as();
		
		List<PersonValueProxy> wpt = wrapList(PersonValueProxy.class, pt);
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
		
		List<Mail> wpt = wrapList(Mail.class, pt);
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
		
		List<Person> wpt = wrapList(Person.class, pt);
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
	
	
	
	
	
	/**
	 * Metodo genérico para codificar una lista de elementos de una clase en especial T.
	 * @param clazz
	 * @param list
	 * @return
	 */
/*	public <T> String encodeList(Class<T> clazz, List<T> list) {

		// para manejar el error de autobean cuando los elementos de la lista no tienen wrapping.
		List<T> wrappedList = wrapList(clazz, list);
		
		AutoBean<ListContainer<T>> lc = personFactory.listContainer();
		ListContainer<T> lcontainer = lc.as();
		lcontainer.setList(list);
		
		String json = AutoBeanCodex.encode(lc).getPayload();
		return json;
	}
	
	
	public <T> List<T> decodeList(Class<T> clazz, String list) {
		AutoBean<ListContainer> bean = AutoBeanCodex.decode(personFactory, ListContainer.class, list);
		ListContainer lc = bean.as();
		List<T> rlist = lc.getElements(clazz);
		return rlist;
	}
*/	
		
	
	
	
	
	
	
	
	
	
	
	
}
