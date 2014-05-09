package ar.com.dcsys.pr.shared;

import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.manager.shared.message.Message;
import ar.com.dcsys.pr.ClientManager;

@ClientManager
public interface TestManager {

	public void trr(List<Person> persons, Receiver<Person> rec);
	
	public void tr(List<Person> persons, Receiver<List<Person>> rec);
	
	public void t(List<Message> m, Receiver<String> rec);
	
	public void test1(String p1, Long p2, Receiver<String> rec);
	
	public void test3(Receiver<String> rec);
	
	public void test4(List<String> lista, Receiver<String> rec);
	
	public void test5(Message m, Receiver<String> rec);
	
}
