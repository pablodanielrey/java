package ar.com.dcsys.gwt.auth.shared;

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
		@Serializer(serializer="ar.com.dcsys.pr.serializers.server.BooleanSerializer", clazz="java.lang.Boolean", type=SerializerType.SERVER)		
})
public interface AuthManagerTransfer extends Manager {

	public void isAuthenticated(Receiver<Boolean> rec);
	public void hasPermission(String perm, Receiver<Boolean> rec);
	
}
