package ar.com.dcsys.gwt.person.shared;

import java.util.List;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.gwt.manager.shared.Manager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.pr.ClientManager;
import ar.com.dcsys.pr.Serializer;
import ar.com.dcsys.pr.SerializerType;

@ClientManager(serializers = {
	@Serializer(serializer="ar.com.dcsys.pr.serializers.shared.VoidSerializer", clazz="java.lang.Void", type=SerializerType.COMBINED),
	@Serializer(serializer="ar.com.dcsys.gwt.data.document.client.DocumentSerializer", clazz="ar.com.dcsys.data.document.Document", type=SerializerType.CLIENT),
	@Serializer(serializer="ar.com.dcsys.gwt.data.document.server.DocumentSerializer", clazz="ar.com.dcsys.data.document.Document", type=SerializerType.SERVER),
	@Serializer(serializer="ar.com.dcsys.gwt.data.document.client.DocumentListSerializer", clazz="java.util.List<ar.com.dcsys.data.document.Document>", type=SerializerType.CLIENT),
	@Serializer(serializer="ar.com.dcsys.gwt.data.document.server.DocumentListSerializer", clazz="java.util.List<ar.com.dcsys.data.document.Document>", type=SerializerType.SERVER)
})
public interface ReportsManager extends Manager {

	public void generateAllPersonsReport(Receiver<Void> rec);
	public void findAllReports(Receiver<List<Document>> rec);
	
}
