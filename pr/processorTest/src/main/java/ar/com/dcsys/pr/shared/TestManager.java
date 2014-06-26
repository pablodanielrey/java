package ar.com.dcsys.pr.shared;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.gwt.manager.shared.Manager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.pr.ClientManager;
import ar.com.dcsys.pr.Serializer;
import ar.com.dcsys.pr.SerializerType;

@ClientManager(serializers = {
		@Serializer(serializer="ar.com.dcsys.pr.serializers.shared.StringSerializer", clazz="java.lang.String", type=SerializerType.COMBINED),
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
		@Serializer(serializer="ar.com.dcsys.gwt.data.document.client.DocumentSerializer", clazz="ar.com.dcsys.data.document.Document", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.document.server.DocumentSerializer", clazz="ar.com.dcsys.data.document.Document", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.data.document.client.DocumentListSerializer", clazz="java.util.List<ar.com.dcsys.data.document.Document>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.data.document.server.DocumentListSerializer", clazz="java.util.List<ar.com.dcsys.data.document.Document>", type=SerializerType.SERVER)
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
	
	public void test30(Document d, Receiver<Document> rec);
	public void test31(Document d, Receiver<List<Document>> rec);
	public void test32(List<Document> ds, Receiver<List<Document>> rec);

}
