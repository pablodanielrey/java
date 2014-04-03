package ar.com.dcsys.gwt.person.server.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	
	public synchronized List<String> findAll() {
		Set<String> ks = reports.keySet();
		List<String> al = new ArrayList<String>();
		al.addAll(ks);
		return al;
	}
	
}
