package ar.com.dcsys.pr.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.device.Device;
import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.log.AttLog;
import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.period.WorkedHours;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.data.report.Report;
import ar.com.dcsys.data.report.ReportSummary;
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
			Person p = new Person();
			p.setDni(UUID.randomUUID().toString());
			l.add(p);
		}
		rec.onSuccess(l);
	}

	@Override
	public void test7(String id, Receiver<Person> rec) {
		Person p = new Person();
		p.setDni(id);
		rec.onSuccess(p);
	}

	@Override
	public void test8(String id, Receiver<List<Person>> rec) {
		List<Person> l = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Person p = new Person();
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
		
		Mail mb = new Mail();
		mb.setMail("pablo@econo");
		
		MailChange mc = new MailChange();
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
		
		Mail m = new Mail();
		m.setMail("algo@econo");
		
		List<MailChange> l = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			MailChange mc = new MailChange();
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
			Person p = new Person();
			p.setDni(UUID.randomUUID().toString());
			l.add(p);
		}
		rec.onSuccess(l);
	}
	
	@Override
	public void test41(Person person, Receiver<List<PeriodAssignation>> receiver) {
		logger.log(Level.INFO, person.toString());
		List<PeriodAssignation> pa = new ArrayList<>();
		PeriodAssignation p = new PeriodAssignation();
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
	
	@Override
	public void test51(Justification j, Receiver<List<Justification>> rec) {
		logger.log(Level.INFO, j.toString());
		rec.onSuccess(Arrays.asList(j,j,j));
	}
	
	@Override
	public void test52(List<Justification> js, Receiver<List<Justification>> rec) {
		logger.log(Level.INFO, js.toString());
		rec.onSuccess(js);
	}
	
	@Override
	public void test53(JustificationDate jd, Receiver<JustificationDate> rec) {
		logger.log(Level.INFO, jd.toString());
		rec.onSuccess(jd);
	}
	
	@Override
	public void test54(JustificationDate jd, Receiver<List<JustificationDate>> rec) {
		logger.log(Level.INFO, jd.toString());
		rec.onSuccess(Arrays.asList(jd,jd,jd));
	}
	
	@Override
	public void test55(List<JustificationDate> jds, Receiver<List<JustificationDate>> rec) {
		logger.log(Level.INFO, jds.toString());
		rec.onSuccess(jds);
	}
	
	@Override
	public void test56(GeneralJustificationDate gjd, Receiver<GeneralJustificationDate> rec) {
		logger.log(Level.INFO, gjd.toString());
		rec.onSuccess(gjd);
	}
	
	@Override
	public void test57(GeneralJustificationDate gjd, Receiver<List<GeneralJustificationDate>> rec) {
		logger.log(Level.INFO, gjd.toString());
		rec.onSuccess(Arrays.asList(gjd,gjd,gjd));
	}
	
	@Override
	public void test58(List<GeneralJustificationDate> gjds, Receiver<List<GeneralJustificationDate>> rec) {
		logger.log(Level.INFO, gjds.toString());
		rec.onSuccess(gjds);
	}
	
	@Override
	public void test60(Person person, Receiver<List<PeriodAssignation>> rec) {
		logger.log(Level.INFO, person.toString());
		
		List<PeriodAssignation> pal = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			PeriodAssignation pa = new PeriodAssignation();
			pa.setId(String.valueOf(i));
			pa.setPerson(person);
			pa.setStart(new Date());
			pa.setType(PeriodType.SYSTEMS);
			pal.add(pa);
		}
		rec.onSuccess(pal);
	}
	
	@Override
	public void test61(Receiver<List<PeriodType>> rec) {
		logger.log(Level.INFO, "test61");
		rec.onSuccess(Arrays.asList(PeriodType.values()));
	}
	
	@Override
	public void test62(Person person, PeriodAssignation periodAssignation, Receiver<Void> receiver) {
		logger.log(Level.INFO, person.toString());
		logger.log(Level.INFO,periodAssignation.toString());
		receiver.onSuccess(null);
	}
	
	
	@Override
	public void test63(Date start, Date end, List<Person> persons, Receiver<ReportSummary> rec) {
		logger.log(Level.INFO, start.toString());
		logger.log(Level.INFO, end.toString());
		logger.log(Level.INFO, String.valueOf(persons.size()));
		
		
		Device d = new Device();
		d.setDescription("reloj1");
		d.setEnabled(true);
		d.setGateway("163.10.17.1");
		d.setId("id-reloj1");
		d.setMac("sd:sd:sd:34:s:34:22");
		d.setName("reloj");
		d.setNetmask("255.255.255.255");
		
		AttLog l = new AttLog();
		l.setDate(new Date());
		l.setDevice(d);
		l.setId("log1sdsadsadadsdsa");
		l.setPerson(persons.get(0));
		l.setVerifyMode(1l);
		
		WorkedHours wh = new WorkedHours();
		wh.setInLog(l);
		wh.setOutLog(l);
		wh.setLogs(Arrays.asList(l,l,l,l));
		
		
		Period p = new Period();
		p.setEnd(new Date());
		p.setStart(new Date());
		p.setPerson(persons.get(0));
		p.setWorkedHours(Arrays.asList(wh,wh,wh));
		
		Justification j = new Justification();
		j.setCode("dsdsd");
		j.setDescription("descripcion j");
		j.setId("id-j");
		
		GeneralJustificationDate gjd = new GeneralJustificationDate();
		gjd.setEnd(new Date());
		gjd.setStart(new Date());
		gjd.setNotes("notas");
		gjd.setJustification(j);
		
		JustificationDate jd = new JustificationDate();
		jd.setStart(new Date());
		jd.setEnd(new Date());
		jd.setJustification(j);
		jd.setId("id-jd");
		jd.setPerson(persons.get(0));
		jd.setNotes("algo de notas");
		
		Report r = new Report();
		r.setPerson(persons.get(0));
		r.setPeriod(p);
		r.setGjustifications(Arrays.asList(gjd,gjd,gjd));
		r.setJustifications(Arrays.asList(jd,jd,jd));
	
		ReportSummary rs = new ReportSummary();
		rs.addReport(r);
		rs.addReport(r);
		rs.addReport(r);
		rs.addReport(r);
		rs.setStart(new Date());
		rs.setEnd(new Date());
		
		rec.onSuccess(rs);
	}
		
}
