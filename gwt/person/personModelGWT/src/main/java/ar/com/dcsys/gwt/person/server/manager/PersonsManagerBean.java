package ar.com.dcsys.gwt.person.server.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailBean;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.messages.shared.Transport;
import ar.com.dcsys.gwt.person.shared.PersonValueProxy;
import ar.com.dcsys.gwt.person.shared.PersonValueProxyImpl;
import ar.com.dcsys.gwt.person.shared.manager.PersonsManager;

public class PersonsManagerBean implements PersonsManager {

	private final ar.com.dcsys.model.PersonsManager personsManager;
	
	@Inject
	public PersonsManagerBean(ar.com.dcsys.model.PersonsManager personsManager) {
		this.personsManager = personsManager;
	}
	
	@Override
	public void setTransport(Transport t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getLoggedPerson(Receiver<Person> rec) {
		try {
			Person person = personsManager.getLoggedPerson();
			rec.onSuccess(person);
		} catch (PersonException e) {
			rec.onError(e.getMessage());
		}
	}

	@Override
	public void persist(Person person, Receiver<String> receiver) {
		try {
			personsManager.persist(person);
		} catch (PersonException e) {
			receiver.onError(e.getMessage());
		}
	}

	@Override
	public void findAll(Receiver<List<Person>> receiver) {
		try {
			List<Person> persons = personsManager.findAll();
			receiver.onSuccess(persons);
		} catch (PersonException e) {
			receiver.onError(e.getMessage());
		}
	}

	
	private List<PersonValueProxy> toPersonValue(List<Person> persons) {
		List<PersonValueProxy> pvs = new ArrayList<>();
		for (Person p : persons) {
			PersonValueProxy pv = new PersonValueProxyImpl();
			pv.setId(p.getId());
			pv.setDni(p.getDni());
			pv.setName(p.getName());
			pv.setLastName(p.getLastName());
			pvs.add(pv);
		}			
		return pvs;
	}
	
	@Override
	public void findAllPersonValue(Receiver<List<PersonValueProxy>> rec) {
		try {
			List<Person> persons = personsManager.findAll();
			rec.onSuccess(toPersonValue(persons));
		} catch (PersonException e) {
			rec.onError(e.getMessage());
		}
	}

	@Override
	public void findAllPersonValue(List<PersonType> types, Receiver<List<PersonValueProxy>> rec) {
		try {
			List<Person> persons = personsManager.findAllBy(types);
			rec.onSuccess(toPersonValue(persons));
		} catch (PersonException e) {
			rec.onError(e.getMessage());
		}
	}

	@Override
	public void findMails(Person p, Receiver<List<Mail>> rec) {
		Mail m = new MailBean();
		m.setMail("prueba@generar-codigo-en-el-dominio.com");
		
		List<Mail> mails = new ArrayList<>();							// todavía no esta en el modelo asi que lo dejo por ahora para implementarlo despues.
		mails.add(m);
		
		rec.onSuccess(mails);
	}

	@Override
	public void addMail(Person p, Mail m, Receiver<Void> rec) {
		// no esta implementado asi que llamo al rec sin nada
		rec.onSuccess(null);
	}

	@Override
	public void findAllTypes(Receiver<List<PersonType>> rec) {
		List<PersonType> types = Arrays.asList(PersonType.values());
		rec.onSuccess(types);
	}

	@Override
	public void findByDni(String dni, Receiver<Person> rec) {
		try {
			Person person = personsManager.findByDni(dni);
			rec.onSuccess(person);
		} catch (PersonException e) {
			rec.onError(e.getMessage());
		}
	}

	@Override
	public void findById(String id, Receiver<Person> rec) {
		try {
			Person person = personsManager.findById(id);
			rec.onSuccess(person);
		} catch (PersonException e) {
			rec.onError(e.getMessage());
		}
	}

	@Override
	public void report(Receiver<Document> rec) {
		// TODO: rec --
		rec.onSuccess(null);
	}

	@Override
	public void findAllReports(Receiver<List<Document>> rec) {
		rec.onSuccess(new ArrayList<Document>());
	}

}
