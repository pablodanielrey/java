package ar.com.dcsys.gwt.assistance.shared;

import java.util.List;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.manager.shared.Manager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.pr.ClientManager;
import ar.com.dcsys.pr.Serializer;
import ar.com.dcsys.pr.SerializerType;

@ClientManager(serializers={
		@Serializer(serializer="ar.com.dcsys.pr.serializers.shared.VoidSerializer", clazz="java.lang.Void", type=SerializerType.COMBINED),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.shared.StringSerializer", clazz="java.lang.String", type=SerializerType.COMBINED),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.client.StringListSerializer", clazz="java.util.List<java.lang.String>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.server.StringListSerializer", clazz="java.util.List<java.lang.String>", type=SerializerType.SERVER),	
		
		@Serializer(serializer="ar.com.dcsys.pr.serializers.client.BooleanSerializer", clazz="java.lang.Boolean", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.server.BooleanSerializer", clazz="java.lang.Boolean", type=SerializerType.SERVER),

		@Serializer(serializer="ar.com.dcsys.gwt.person.client.PersonTypeSerializer", clazz="ar.com.dcsys.data.person.PersonType", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.person.server.PersonTypeSerializer", clazz="ar.com.dcsys.data.person.PersonType", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.PersonSerializer", clazz="ar.com.dcsys.data.person.Person", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.person.server.PersonSerializer", clazz="ar.com.dcsys.data.person.Person", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.PersonListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.Person>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.person.server.PersonListSerializer", clazz="java.util.List<ar.com.dcsys.data.person.Person>", type=SerializerType.SERVER),
		
		@Serializer(serializer="ar.com.dcsys.auth.server.FingerprintSerializer", clazz="ar.com.dcsys.security.Fingerprint", type=SerializerType.SERVER)		

		
})
public interface PersonDataManagerTransfer extends Manager {
	
	public void setPin(String personId, String pin, Receiver<Boolean> rec);
	public void enroll(String personId, Receiver<String> rec);
	public void persist(Person person, Receiver<String> rec);
	public void syncPersons(Receiver<List<String>> persons);
	public void transferFingerprints(String personId, Receiver<Boolean> rec);
	
}
