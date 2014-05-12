package ar.com.dcsys.pr.server;

import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.manager.shared.message.Message;
import ar.com.dcsys.pr.shared.TestManager;

public class TestManagerBean implements TestManager {

	@Override
	public void trr(List<Person> persons, Receiver<Person> rec) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tr(List<Person> persons, Receiver<List<Person>> rec) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void t(List<Message> m, Receiver<String> rec) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void test1(String p1, Long p2, Receiver<String> rec) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void test3(Receiver<String> rec) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void test4(List<String> lista, Receiver<String> rec) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void test5(Message m, Receiver<String> rec) {
		// TODO Auto-generated method stub
		
	}

}
