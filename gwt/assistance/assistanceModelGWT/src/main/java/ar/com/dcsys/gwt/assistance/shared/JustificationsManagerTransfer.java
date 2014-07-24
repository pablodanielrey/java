package ar.com.dcsys.gwt.assistance.shared;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.justification.GeneralJustificationDate;
import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.data.justification.JustificationDate;
import ar.com.dcsys.data.period.Period;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.manager.shared.Manager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.pr.ClientManager;
import ar.com.dcsys.pr.Serializer;
import ar.com.dcsys.pr.SerializerType;

@ClientManager(serializers={
		@Serializer(serializer="ar.com.dcsys.pr.serializers.shared.VoidSerializer", clazz="java.lang.Void", type=SerializerType.COMBINED),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.shared.StringSerializer", clazz="java.lang.String", type=SerializerType.COMBINED),
	

		@Serializer(serializer="ar.com.dcsys.pr.serializers.client.DateListSerializer", clazz="java.util.List<java.util.Date>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.server.DateListSerializer", clazz="java.util.List<java.util.Date>", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.client.DateSerializer", clazz="java.util.Date", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.server.DateSerializer", clazz="java.util.Date", type=SerializerType.SERVER),


		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.PeriodSerializer", clazz="ar.com.dcsys.data.period.Period", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.PeriodSerializer", clazz="ar.com.dcsys.data.period.Period", type=SerializerType.SERVER),	
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.PeriodListSerializer", clazz="java.util.List<ar.com.dcsys.data.period.Period>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.PeriodListSerializer", clazz="java.util.List<ar.com.dcsys.data.period.Period>", type=SerializerType.SERVER),
		
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.JustificationSerializer", clazz="ar.com.dcsys.data.justification.Justification", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.JustificationSerializer", clazz="ar.com.dcsys.data.justification.Justification", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.JustificationListSerializer", clazz="java.util.List<ar.com.dcsys.data.justification.Justification>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.JustificationListSerializer", clazz="java.util.List<ar.com.dcsys.data.justification.Justification>", type=SerializerType.SERVER),
		
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.JustificationDateSerializer", clazz="ar.com.dcsys.data.justification.JustificationDate", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.JustificationDateSerializer", clazz="ar.com.dcsys.data.justification.JustificationDate", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.JustificationDateListSerializer", clazz="java.util.List<ar.com.dcsys.data.justification.JustificationDate>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.JustificationDateListSerializer", clazz="java.util.List<ar.com.dcsys.data.justification.JustificationDate>", type=SerializerType.SERVER),
		
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.GeneralJustificationDateSerializer", clazz="ar.com.dcsys.data.justification.GeneralJustificationDate", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.GeneralJustificationDateSerializer", clazz="ar.com.dcsys.data.justification.GeneralJustificationDate", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.client.GeneralJustificationDateListSerializer", clazz="java.util.List<ar.com.dcsys.data.justification.GeneralJustificationDate>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.assistance.server.GeneralJustificationDateListSerializer", clazz="java.util.List<ar.com.dcsys.data.justification.GeneralJustificationDate>", type=SerializerType.SERVER),

		@Serializer(serializer="ar.com.dcsys.gwt.person.client.PersonTypeSerializer", clazz="ar.com.dcsys.data.person.PersonType", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.person.server.PersonTypeSerializer", clazz="ar.com.dcsys.data.person.PersonType", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.PersonSerializer", clazz="ar.com.dcsys.data.person.Person", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.person.server.PersonSerializer", clazz="ar.com.dcsys.data.person.Person", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.PersonListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.Person>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.person.server.PersonListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.Person>", type=SerializerType.SERVER)
		
		
})
public interface JustificationsManagerTransfer extends Manager {
	
	public void getJustifications(Receiver<List<Justification>> receiver);
	
	public void persist(Justification justification, Receiver<Void> receiver);
	
	public void remove(Justification justification, Receiver<Void> receiver);
	
	public void justify(Person person, Date start, Date end, Justification justification, String notes, Receiver<Void> receiver);
	public void justify(Person person, List<Period> periods, Justification justification, String notes, Receiver<Void> receiver);
	public void justify(List<Period> periods, Justification justification, String notes, Receiver<Void> receiver);
	
	public void findBy(List<Person> persons, Date start, Date end, Receiver<List<JustificationDate>> receiver);
	
	public void remove(List<JustificationDate> justifications, Receiver<Void> receiver);
	
	public void persist(List<GeneralJustificationDate> justifications, Receiver<Void> receiver);
	
	public void findGeneralJustificationDateBy(Date start, Date end, Receiver<List<GeneralJustificationDate>> receiver);
	
	public void removeGeneralJustificationDate(List<GeneralJustificationDate> justifications, Receiver<Void> receiver);

}
