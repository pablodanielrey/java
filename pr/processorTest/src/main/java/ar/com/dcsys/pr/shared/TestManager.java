package ar.com.dcsys.pr.shared;

import ar.com.dcsys.gwt.manager.shared.Manager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.pr.ClientManager;
import ar.com.dcsys.pr.Serializer;
import ar.com.dcsys.pr.SerializerType;
import ar.com.dcsys.pr.shared.serializers.StringSerializer;

@ClientManager(serializers = {
		@Serializer(serializer="ar.com.dcsys.pr.shared.serializers.StringSerializer", type=SerializerType.COMBINED, clazz="java.lang.String")
})
public interface TestManager extends Manager {

	/*
	public void testEnum(Receiver<PersonType> rec);
	public void testEnum2(PersonType pt, Receiver<PersonType> rec);
	public void testEnum3(Person p, PersonType pt, Receiver<PersonType> rec);
	public void testEnum4(String id, PersonType pt, Receiver<PersonType> rec);
	*/
	public void test(Receiver<String> rec);
	public void test3(String pepe, Receiver<String> rec);
	
	/*
	public void test1(Person person, Receiver<String> rec);
	public void test2(Receiver<List<String>> rec);
	
	public void test4(List<String> pepe, Receiver<String> rec);
	public void test5(List<Person> pepe, Receiver<String> rec);
	public void test6(Receiver<List<Person>> rec);
	public void test7(String id, Receiver<Person> rec);
	public void test8(String id, Receiver<List<Person>> rec);
	public void test9(Date d, Receiver<List<Date>> rec);
	*/

}
