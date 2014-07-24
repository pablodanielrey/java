package ar.com.dcsys.assistance.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.report.Report;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ReportListSerializer implements CSD<List<Report>> {

	private static final Logger logger = Logger.getLogger(ReportListSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();
	
	
	private class Container {
		List<Report> list = new ArrayList<>();
	}	
	
	
	
	@Override
	public String toJson(List<Report> o) {
		Container sc = new Container();
		for (Report r : o) {
			sc.list.add((Report)r);
		}
		String d = gson.toJson(sc);
		logger.log(Level.WARNING, "gson : " + d);
		return d;
	}

	@Override
	public List<Report> read(String json) {
		logger.log(Level.WARNING,"gson : " + json);
		TypeToken<Container> type = new TypeToken<Container>() {};
		Container sc = gson.fromJson(json, type.getType());
		List<Report> rs = new ArrayList<>();
		for (Report r : sc.list) {
			rs.add(r);
		}
		return rs;
	}	

}
