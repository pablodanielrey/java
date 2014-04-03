package ar.com.dcsys.gwt.person.server.reports;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

@Singleton
public class ReportContainer {

	private final Map<String,byte[]> reports = new HashMap<>();
	
	public synchronized void addReport(String id, byte[] data) {
		reports.put(id, data);
	}
	
	public synchronized byte[] getReport(String id) {
		if (!reports.containsKey(id)) {
			return null;
		}
		return reports.get(id);
	}
	
	
}
