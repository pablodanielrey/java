package ar.com.dcsys.pr.shared;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailChange;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.gwt.manager.shared.Manager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.pr.ClientManager;
import ar.com.dcsys.pr.Serializer;
import ar.com.dcsys.pr.SerializerType;

@ClientManager(serializers = {
		@Serializer(serializer="ar.com.dcsys.pr.serializers.shared.StringSerializer", clazz="java.lang.String", type=SerializerType.COMBINED),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.shared.VoidSerializer", clazz="java.lang.Void", type=SerializerType.COMBINED),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.client.StringListSerializer", clazz="java.util.List<java.lang.String>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.server.StringListSerializer", clazz="java.util.List<java.lang.String>", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.client.DateListSerializer", clazz="java.util.List<java.util.Date>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.server.DateListSerializer", clazz="java.util.List<java.util.Date>", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.client.DateSerializer", clazz="java.util.Date", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.server.DateSerializer", clazz="java.util.Date", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.client.BooleanSerializer", clazz="java.lang.Boolean", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.server.BooleanSerializer", clazz="java.lang.Boolean", type=SerializerType.SERVER),
		
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.PersonTypeSerializer", clazz="ar.com.dcsys.data.person.PersonType", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.PersonSerializer", clazz="ar.com.dcsys.data.person.Person", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.person.server.PersonSerializer", clazz="ar.com.dcsys.data.person.Person", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.PersonListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.Person>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.person.server.PersonListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.Person>", type=SerializerType.SERVER),

		@Serializer(serializer="ar.com.dcsys.gwt.person.client.GroupTypeSerializer", clazz="ar.com.dcsys.data.group.PersonType", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.GroupSerializer", clazz="ar.com.dcsys.data.group.Group", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.person.server.GroupSerializer", clazz="ar.com.dcsys.data.group.Group", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.GroupTypeListSerializer", clazz="java.util.List<ar.com.dcsys.data.group.GroupType>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.person.server.GroupTypeListSerializer", clazz="java.util.List<ar.com.dcsys.data.group.GroupType>", type=SerializerType.SERVER),
		
		
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.MailSerializer", clazz="ar.com.dcsys.data.person.Mail", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.person.server.MailSerializer", clazz="ar.com.dcsys.data.person.Mail", type=SerializerType.SERVER),	
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.MailListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.Mail>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.person.server.MailListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.Mail>", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.MailChangeSerializer", clazz="ar.com.dcsys.data.person.MailChange", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.person.server.MailChangeSerializer", clazz="ar.com.dcsys.data.person.MailChange", type=SerializerType.SERVER),	
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.MailChangeListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.MailChange>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.person.server.MailChangeListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.MailChange>", type=SerializerType.SERVER),			
		@Serializer(serializer="ar.com.dcsys.gwt.data.document.client.DocumentSerializer", clazz="ar.com.dcsys.data.document.Document", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.document.server.DocumentSerializer", clazz="ar.com.dcsys.data.document.Document", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.data.document.client.DocumentListSerializer", clazz="java.util.List<ar.com.dcsys.data.document.Document>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.document.server.DocumentListSerializer", clazz="java.util.List<ar.com.dcsys.data.document.Document>", type=SerializerType.SERVER),	
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.JustificationSerializer", clazz="ar.com.dcsys.data.justification.Justification", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.JustificationSerializer", clazz="ar.com.dcsys.data.justification.Justification", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.JustificationListSerializer", clazz="java.util.List<ar.com.dcsys.data.justification.Justification>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.JustificationListSerializer", clazz="java.util.List<ar.com.dcsys.data.justification.Justification>", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.JustificationDateSerializer", clazz="ar.com.dcsys.data.justification.JustificationDate", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.JustificationDateSerializer", clazz="ar.com.dcsys.data.justification.JustificationDate", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.JustificationDateListSerializer", clazz="java.util.List<ar.com.dcsys.data.justification.JustificationDate>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.JustificationDateListSerializer", clazz="java.util.List<ar.com.dcsys.data.justification.JustificationDate>", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.PeriodTypeSerializer", clazz="ar.com.dcsys.data.period.PeriodType", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.PeriodTypeListSerializer", clazz="java.util.List<ar.com.dcsys.data.period.PeriodType>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.PeriodTypeListSerializer", clazz="java.util.List<ar.com.dcsys.data.period.PeriodType>", type=SerializerType.SERVER),	
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.PeriodAssignationListSerializer", clazz="java.util.List<ar.com.dcsys.data.period.PeriodAssignation>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.PeriodAssignationListSerializer", clazz="java.util.List<ar.com.dcsys.data.period.PeriodAssignation>", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.PeriodAssignationSerializer", clazz="ar.com.dcsys.data.period.PeriodAssignation", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.PeriodAssignationSerializer", clazz="ar.com.dcsys.data.period.PeriodAssignation", type=SerializerType.SERVER)	
		
		
})
public interface TestManager extends Manager {


	public void testEnum(Receiver<PersonType> rec);
	public void testEnum2(PersonType pt, Receiver<PersonType> rec);
	public void testEnum3(Person p, PersonType pt, Receiver<PersonType> rec);
	public void testEnum4(String id, PersonType pt, Receiver<PersonType> rec);
	
	public void test(Receiver<String> rec);
	public void test1(Person person, Receiver<String> rec);
	public void test2(Receiver<List<String>> rec);
	public void test3(String pepe, Receiver<String> rec);
	public void test4(List<String> pepe, Receiver<String> rec);
	public void test5(List<Person> pepe, Receiver<String> rec);
	public void test6(Receiver<List<Person>> rec);
	public void test7(String id, Receiver<Person> rec);
	public void test8(String id, Receiver<List<Person>> rec);
	public void test9(Date d, Receiver<List<Date>> rec);
	public void test10(Boolean b, Receiver<Boolean> rec);
	
	public void test20(Mail m, Receiver<Mail> rec);
	public void test21(Mail m, Receiver<List<Mail>> rec);
	public void test22(List<Mail> ms, Receiver<List<Mail>> rec);
	
	public void test30(Document d, Receiver<Document> rec);
	public void test31(Document d, Receiver<List<Document>> rec);
	public void test32(List<Document> ds, Receiver<List<Document>> rec);

	public void test33(Void v, Receiver<Void> rec);

	public void test34(MailChange mc, Receiver<Void> rec);
	public void test35(Void v, Receiver<MailChange> rec);
	public void test36(MailChange mc, Receiver<MailChange> rec);
	public void test37(Receiver<List<MailChange>> rec);
	public void test38(List<MailChange> mc, Receiver<List<MailChange>> rec);
	
	public void test40(Group group, Receiver<List<Person>> receiver);
	public void test41(Person person, Receiver<List<PeriodAssignation>> receiver);
	public void test42(Receiver<List<PeriodType>> receiver);
	public void test43(Person person, PeriodAssignation periodAssignation, Receiver<Void> receiver); 
	
	
	public void test50(Justification j, Receiver<Justification> rec);
	public void test51(Justification j, Receiver<List<Justification>> rec);
	public void test52(List<Justification> js, Receiver<List<Justification>> rec);
	
	public void test53(JustificationDate jd, Receiver<JustificationDate> rec);
	public void test54(JustificationDate jd, Receiver<List<JustificationDate>> rec);
	public void test55(List<JustificationDate> jds, Receiver<List<JustificationDate>> rec);
	
}
