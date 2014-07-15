package ar.com.dcsys.gwt.person.shared;

import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.gwt.manager.shared.Manager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.pr.ClientManager;
import ar.com.dcsys.pr.Serializer;
import ar.com.dcsys.pr.SerializerType;

@ClientManager(serializers = {
		@Serializer(serializer="ar.com.dcsys.pr.serializers.shared.VoidSerializer", clazz="java.lang.Void", type=SerializerType.COMBINED),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.shared.StringSerializer", clazz="java.lang.String", type=SerializerType.COMBINED),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.client.StringListSerializer", clazz="java.util.List<java.lang.String>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.pr.serializers.server.StringListSerializer", clazz="java.util.List<java.lang.String>", type=SerializerType.SERVER),
		
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.GroupTypeSerializer", clazz="ar.com.dcsys.data.group.PersonType", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.person.server.GroupTypeSerializer", clazz="ar.com.dcsys.data.group.PersonType", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.GroupTypeListSerializer", clazz="java.util.List<ar.com.dcsys.data.group.GroupType>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.person.server.GroupTypeListSerializer", clazz="java.util.List<ar.com.dcsys.data.group.GroupType>", type=SerializerType.SERVER),		
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.GroupSerializer", clazz="ar.com.dcsys.data.group.Group", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.person.server.GroupSerializer", clazz="ar.com.dcsys.data.group.Group", type=SerializerType.SERVER),
		@Serializer(serializer="ar.com.dcsys.gwt.person.client.GroupListSerializer", clazz="java.util.List<ar.com.dcsys.data.group.Group>", type=SerializerType.CLIENT),
		@Serializer(serializer="ar.com.dcsys.gwt.person.server.GroupListSerializer", clazz="java.util.List<ar.com.dcsys.data.group.Group>", type=SerializerType.SERVER)
})
public interface GroupsManager extends Manager {

	public void findAllGroupTypes(Receiver<List<GroupType>> types);
	public void findAll(Receiver<List<Group>> groups);	
	
}
