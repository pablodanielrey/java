package ar.com.dcsys.assistance.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class PeriodAssignationListSerializer implements CSD<List<PeriodAssignation>> {

	private static final Logger logger = Logger.getLogger(PeriodAssignationListSerializer.class.getName());
	
	private final Gson gson = (new GsonBuilder()).setDateFormat("HH:mm:ss dd/MM/yyyy")
												 .create();
	
	private class Container {
		List<PeriodAssignation> list;
	}	
	
	
	private List<PeriodAssignation> toPeriodAssignationBeanList(List<PeriodAssignation> l) {
		List<PeriodAssignation> ps = new ArrayList<PeriodAssignation>();
		for (PeriodAssignation p : l) {
			ps.add((PeriodAssignation)p);
		}
		return ps;
	}

	private List<PeriodAssignation> toPeriodAssignationList(List<PeriodAssignation> l) {
		List<PeriodAssignation> ps = new ArrayList<PeriodAssignation>();
		for (PeriodAssignation p : l) {
			ps.add(p);
		}
		return ps;
	}
		
	
	@Override
	public String toJson(List<PeriodAssignation> o) {
		Container sc = new Container();
		sc.list = toPeriodAssignationBeanList(o);
		String d = gson.toJson(sc);
		logger.log(Level.WARNING, "gson : " + d);
		return d;
	}

	@Override
	public List<PeriodAssignation> read(String json) {
		logger.log(Level.WARNING,"gson : " + json);

		TypeToken<Container> type = new TypeToken<Container>() {};
		Container sc = gson.fromJson(json, type.getType());
		return toPeriodAssignationList(sc.list);
	}	
	
}
