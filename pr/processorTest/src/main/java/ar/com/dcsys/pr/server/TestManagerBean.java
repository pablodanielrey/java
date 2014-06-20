package ar.com.dcsys.pr.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.messages.shared.Transport;
import ar.com.dcsys.pr.shared.TestManager;

public class TestManagerBean implements TestManager {

	private static Logger logger = Logger.getLogger(TestManagerBean.class.getName());

	@Override
	public void setTransport(Transport t) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void testEnum(Receiver<PersonType> rec) {
		rec.onSuccess(PersonType.PERSONAL);
	}

	@Override
	public void testEnum2(PersonType pt, Receiver<PersonType> rec) {
		rec.onSuccess(pt);
	}
	
	
	
	@Override
	public void test(Receiver<String> rec) {
		rec.onSuccess("funca");
	}
	
	
	@Override
	public void test3(String pepe, Receiver<String> rec) {
		
		logger.log(Level.SEVERE, "mensaje : " + pepe);
		logger.log(Level.SEVERE, "respondiendo : respuesta");
		
		rec.onSuccess("respuesta");
	}

	@Override
	public void test4(List<String> pepe, Receiver<String> rec) {
		logger.log(Level.SEVERE, "mensaje : " + pepe.size());
		
		rec.onSuccess(pepe.get(pepe.size() - 1));
	}	
	
	/*
	@Override
	public void test1(Person person, Receiver<String> rec) {

		logger.log(Level.SEVERE, "mensaje : " + person.toString());
		
		rec.onSuccess(person.getDni());
		
	}



	@Override
	public void test5(List<Person> pepe, Receiver<String> rec) {
		logger.log(Level.SEVERE, "mensaje : " + pepe.size());
		
		rec.onSuccess((pepe.get(pepe.size() - 1)).getDni());
	}

*/


	
	/*
	
	@Override
	public void test2(Receiver<List<String>> rec) {
		List<String> l = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			l.add(UUID.randomUUID().toString());
		}
		rec.onSuccess(l);
	}

	@Override
	public void test6(Receiver<List<Person>> rec) {
		List<Person> l = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			PersonBean p = new PersonBean();
			p.setDni(UUID.randomUUID().toString());
			l.add(p);
		}
		rec.onSuccess(l);
	}

	@Override
	public void test7(String id, Receiver<Person> rec) {
		PersonBean p = new PersonBean();
		p.setDni(id);
		rec.onSuccess(p);
	}

	@Override
	public void test8(String id, Receiver<List<Person>> rec) {
		List<Person> l = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			PersonBean p = new PersonBean();
			p.setDni(id + " " + UUID.randomUUID().toString());
			l.add(p);
		}
		rec.onSuccess(l);
	}




	@Override
	public void testEnum3(Person p, PersonType pt, Receiver<PersonType> rec) {
		rec.onSuccess(pt);
	}

	@Override
	public void testEnum4(String id, PersonType pt, Receiver<PersonType> rec) {
		rec.onSuccess(pt);
	}


	@Override
	public void test9(Date d, Receiver<List<Date>> rec) {
		logger.log(Level.INFO, d.toString());
		rec.onSuccess(Arrays.asList(new Date(), new Date(), new Date()));
	}
	
	*/

}
