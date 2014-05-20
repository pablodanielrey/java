package ar.com.dcsys.gwt.person.server;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.data.document.DocumentBean;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.messages.shared.Transport;
import ar.com.dcsys.gwt.person.shared.ReportsManager;
import ar.com.dcsys.model.DocumentsManager;
import ar.com.dcsys.model.reports.ReportExportType;
import ar.com.dcsys.model.reports.ReportsGenerator;


public class ReportsManagerBean implements ReportsManager {

	private final ReportsGenerator reportsGenerator;
	private final DocumentsManager documentsManager;
	
	@Inject
	public ReportsManagerBean(ReportsGenerator reportsGenerator, DocumentsManager documentsManager) {
		this.reportsGenerator = reportsGenerator;
		this.documentsManager = documentsManager;
	}
	
	@Override
	public void setTransport(Transport t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateAllPersonsReport(Receiver<Void> rec) {

		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			reportsGenerator.reportPersons(out,ReportExportType.PDF);
		
			byte[] content = out.toByteArray();
			
			DocumentBean d = new DocumentBean();
			d.setName("Reporte de personas");
			d.setCreated(new Date());
			d.setContent(content);
			
			documentsManager.persist(d);
			
			rec.onSuccess(null);
			
		} catch (Exception e) {
			rec.onError(e.getMessage());
		}		
		
		
	}

	@Override
	public void findAllReports(Receiver<List<Document>> rec) {

		try {
			List<Document> ds = new ArrayList<>();
			List<String> reports = documentsManager.findAll();
			for (String id : reports) {
				Document d = documentsManager.findByIdWithoutContent(id);
				ds.add(d);
			}
			rec.onSuccess(ds);
			
		} catch (Exception e) {
			rec.onError(e.getMessage());
		}		
		
	}

}
