package ar.com.dcsys.gwt.person.client.manager;

import java.util.List;

import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.manager.shared.Receiver;

public interface MailChangesManager {
	
	public void persist(MailChange mail, Person person, final Receiver<String> receiver);
	public void findAllBy(Person person, final Receiver<List<MailChange>> receiver);

}
