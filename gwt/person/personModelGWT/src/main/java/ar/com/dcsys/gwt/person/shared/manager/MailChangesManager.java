package ar.com.dcsys.gwt.person.shared.manager;

import java.util.List;

import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.manager.shared.Manager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.pr.ClientManager;

@ClientManager
public interface MailChangesManager extends Manager {
	
	public void persist(MailChange mail, Person person, final Receiver<Void> receiver);
	public void remove(MailChange mail, final Receiver<Void> receiver);
	public void findAllBy(Person person, final Receiver<List<MailChange>> receiver);

}
