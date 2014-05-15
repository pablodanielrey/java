package ar.com.dcsys.pr.shared;

import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.manager.shared.Manager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.pr.ClientManager;

@ClientManager
public interface TestManager extends Manager {

	public void test1(Person person, Receiver<String> rec);
	public void test3(String pepe, Receiver<String> rec);
	public void test4(List<String> pepe, Receiver<String> rec);
	public void test5(List<Person> pepe, Receiver<String> rec);
	
}
