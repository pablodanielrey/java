package ar.com.dcsys.pr.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodAssignationBean;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailBean;
import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.MailChangeBean;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonBean;
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
	public void testEnum3(Person p, PersonType pt, Receiver<PersonType> rec) {
		rec.onSuccess(pt);
	}	
	
	@Override
	public void testEnum4(String id, PersonType pt, Receiver<PersonType> rec) {
		rec.onSuccess(pt);
	}
	
	
	
	
	@Override
	public void test(Receiver<String> rec) {
		rec.onSuccess("funca");
	}
	
	@Override
	public void test1(Person person, Receiver<String> rec) {

		logger.log(Level.SEVERE, "mensaje : " + person.toString());
		
		rec.onSuccess(person.getDni());
		
	}
	
	@Override
	public void test2(Receiver<List<String>> rec) {
		List<String> l = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			l.add(UUID.randomUUID().toString());
		}
		rec.onSuccess(l);
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
	
	@Override
	public void test5(List<Person> pepe, Receiver<String> rec) {
		logger.log(Level.SEVERE, "mensaje : " + pepe.size());
		
		rec.onSuccess((pepe.get(pepe.size() - 1)).getDni());
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
	public void test9(Date d, Receiver<List<Date>> rec) {
		logger.log(Level.INFO, d.toString());
		rec.onSuccess(Arrays.asList(new Date(), new Date(), new Date()));
	}
	
	@Override
	public void test10(Boolean d, Receiver<Boolean> rec) {
		logger.log(Level.INFO, d.toString());
		rec.onSuccess(d);
	}
	
	@Override
	public void test20(Mail m, Receiver<Mail> rec) {
		logger.log(Level.INFO, m.toString());
		rec.onSuccess(m);		
	}
	
	@Override
	public void test21(Mail m, Receiver<List<Mail>> rec) {
		logger.log(Level.INFO, m.toString());
		rec.onSuccess(Arrays.asList(m, m, m));
	}
	
	@Override
	public void test22(List<Mail> ms, Receiver<List<Mail>> rec) {
		logger.log(Level.INFO, ms.toString());
		rec.onSuccess(ms);
	}
	
	
	@Override
	public void test30(Document d, Receiver<Document> rec) {
		logger.log(Level.INFO, d.toString());
		rec.onSuccess(d);
	}
	
	@Override
	public void test31(Document d, Receiver<List<Document>> rec) {
		logger.log(Level.INFO, d.toString());
		rec.onSuccess(Arrays.asList(d, d, d));
	}
	
	@Override
	public void test32(List<Document> ds, Receiver<List<Document>> rec) {
		logger.log(Level.INFO, ds.toString());
		rec.onSuccess(ds);
	}
	
	@Override
	public void test33(Void v, Receiver<Void> rec) {
		if (v == null) {
			logger.log(Level.INFO, "null");
		} else {
			logger.log(Level.INFO, v.toString());
		}
		rec.onSuccess(v);
	}
	
	@Override
	public void test34(MailChange v, Receiver<Void> rec) {
		if (v == null) {
			logger.log(Level.INFO, "null");
		} else {
			logger.log(Level.INFO, v.toString());
		}
		rec.onSuccess(null);
	}

	@Override
	public void test35(Void v, Receiver<MailChange> rec) {
		if (v == null) {
			logger.log(Level.INFO, "null");
		} else {
			logger.log(Level.INFO, v.toString());
		}
		
		MailBean mb = new MailBean();
		mb.setMail("pablo@econo");
		
		MailChangeBean mc = new MailChangeBean();
		mc.setConfirmed(false);
		mc.setMail(mb);
		mc.setPersonId("dsfdfdfdf");
		mc.setToken("sdfdf3434fgwc423f4fewf");
		
		rec.onSuccess(mc);
	}
	
	@Override
	public void test36(MailChange v, Receiver<MailChange> rec) {
		if (v == null) {
			logger.log(Level.INFO, "null");
		} else {
			logger.log(Level.INFO, v.toString());
		}
		rec.onSuccess(v);
	}
	
	
	@Override
	public void test37(Receiver<List<MailChange>> rec) {
		
		MailBean m = new MailBean();
		m.setMail("algo@econo");
		
		List<MailChange> l = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			MailChangeBean mc = new MailChangeBean();
			mc.setMail(m);
			mc.setConfirmed(false);
			mc.setPersonId("pepe1s");
			mc.setToken("fdsfsdfsfdsfdsd");
			l.add(mc);
		}
		rec.onSuccess(l);
	}	
	
	
	@Override
	public void test38(List<MailChange> mc, Receiver<List<MailChange>> rec) {
		logger.log(Level.INFO, mc.toString());
		rec.onSuccess(mc);
	}
	
	
	@Override
	public void test40(Group group, Receiver<List<Person>> rec) {
		logger.log(Level.INFO, group.toString());
		List<Person> l = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			PersonBean p = new PersonBean();
			p.setDni(UUID.randomUUID().toString());
			l.add(p);
		}
		rec.onSuccess(l);
	}
	
	@Override
	public void test41(Person person, Receiver<List<PeriodAssignation>> receiver) {
		logger.log(Level.INFO, person.toString());
		List<PeriodAssignation> pa = new ArrayList<>();
		PeriodAssignationBean p = new PeriodAssignationBean();
		p.setId("sdfdsfsdf");
		p.setPerson(null);
		p.setStart(new Date());
		p.setType(PeriodType.DAILY);
		pa.add(p);
		receiver.onSuccess(pa);
	}
	
	@Override
	public void test42(Receiver<List<PeriodType>> receiver) {
		receiver.onSuccess(Arrays.asList(PeriodType.DAILY,PeriodType.NULL, PeriodType.SYSTEMS, PeriodType.WATCHMAN));
	}
	
	@Override
	public void test43(Person person, PeriodAssignation periodAssignation, Receiver<Void> receiver) {
		logger.log(Level.INFO, person.toString());
		logger.log(Level.INFO, periodAssignation.toString());
		receiver.onSuccess(null);
	}
	
	
	
	@Override
	public void test50(Justification j, Receiver<Justification> rec) {
		logger.log(Level.INFO, j.toString());
		rec.onSuccess(j);
	}
	
	/*@Override
	public void test51(Justification j, Receiver<List<Justification>> rec) {
		logger.log(Level.INFO, j.toString());
		rec.onSuccess(Arrays.asList(j,j,j));
	}
	
	@Override
	public void test52(List<Justification> js, Receiver<List<Justification>> rec) {
		logger.log(Level.INFO, js.toString());
		rec.onSuccess(js);
	}*/
		
}
