package ar.com.dcsys.gwt.assistance.shared;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.report.ReportSummary;
import ar.com.dcsys.gwt.manager.shared.Manager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.pr.ClientManager;
import ar.com.dcsys.pr.Serializer;
import ar.com.dcsys.pr.SerializerType;

@ClientManager(serializers={
		@Serializer(serializer="ar.com.dcsys.pr.serializers.shared.VoidSerializer", clazz="java.lang.Void", type=SerializerType.COMBINED),

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
		
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.PeriodTypeSerializer", clazz="ar.com.dcsys.data.period.PeriodType", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.PeriodTypeListSerializer", clazz="java.util.List<ar.com.dcsys.data.period.PeriodType>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.PeriodTypeListSerializer", clazz="java.util.List<ar.com.dcsys.data.period.PeriodType>", type=SerializerType.SERVER),	
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.PeriodAssignationListSerializer", clazz="java.util.List<ar.com.dcsys.data.period.PeriodAssignation>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.PeriodAssignationListSerializer", clazz="java.util.List<ar.com.dcsys.data.period.PeriodAssignation>", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.PeriodAssignationSerializer", clazz="ar.com.dcsys.data.period.PeriodAssignation", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.PeriodAssignationSerializer", clazz="ar.com.dcsys.data.period.PeriodAssignation", type=SerializerType.SERVER),
		
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.ReportSummarySerializer", clazz="ar.com.dcsys.data.report.ReportSummary", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.ReportSummarySerializer", clazz="ar.com.dcsys.data.report.ReportSummary", type=SerializerType.SERVER),	
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.ReportSerializer", clazz="ar.com.dcsys.data.report.Report", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.ReportSerializer", clazz="ar.com.dcsys.data.report.Report", type=SerializerType.SERVER),	
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.PeriodSerializer", clazz="ar.com.dcsys.data.period.Period", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.PeriodSerializer", clazz="ar.com.dcsys.data.period.Period", type=SerializerType.SERVER),	
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.WorkedHoursSerializer", clazz="ar.com.dcsys.data.period.WorkedHours", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.WorkedHoursSerializer", clazz="ar.com.dcsys.data.period.WorkedHours", type=SerializerType.SERVER),	
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.AttLogSerializer", clazz="ar.com.dcsys.data.log.AttLog", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.AttLogSerializer", clazz="ar.com.dcsys.data.log.AttLog", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.DeviceSerializer", clazz="ar.com.dcsys.data.device.Device", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.DeviceSerializer", clazz="ar.com.dcsys.data.device.Device", type=SerializerType.SERVER)		
		
		
})
public interface PeriodsManagerTransfer extends Manager {
	
	public void findPersonsWithPeriodAssignation(Group group, Receiver<List<Person>> receiver);
	public void findPersonsWithPeriodAssignation(Receiver<List<Person>> receiver);
	
	public void findPeriodsAssignationsBy(Person person, Receiver<List<PeriodAssignation>> receiver);
	public void findAllTypesPeriods(Receiver<List<PeriodType>> receiver);
	
	public void remove(Person person, PeriodAssignation periodAssignation, Receiver<Void> receiver); 
	public void persist(Person person, PeriodAssignation periodAssignation, Receiver<Void> receiver);

	public void findAllPeriods(Date start, Date end, List<Person> persons, Receiver<ReportSummary> rec);
	
}
