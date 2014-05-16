package ar.com.dcsys.gwt.person.shared.manager;

import java.util.List;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.gwt.manager.shared.Manager;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.pr.ClientManager;

@ClientManager
public interface ReportsManager extends Manager {

	public void generateAllPersonsReport(Receiver<Void> rec);
	public void findAllReports(Receiver<List<Document>> rec);
	
}
