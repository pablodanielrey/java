package ar.com.dcsys.gwt.data.assistance.client;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodAssignationBean;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.core.shared.GWT;

public class PeriodAssignationListSerializer implements CSD<List<PeriodAssignation>> {

	private static final Logger logger = Logger.getLogger(PeriodAssignationListSerializer.class.getName());
	
	interface Reader extends name.pehl.piriti.json.client.JsonReader<PeriodAssignationListContainer> { }
	private static final Reader READER = GWT.create(Reader.class);
	
	interface Writer extends name.pehl.piriti.json.client.JsonWriter<PeriodAssignationListContainer> { }
	private static final Writer WRITER = GWT.create(Writer.class);

	
	private List<PeriodAssignationBean> toPeriodAssignationBeanList(List<PeriodAssignation> l) {
		List<PeriodAssignationBean> ps = new ArrayList<PeriodAssignationBean>();
		for (PeriodAssignation p : l) {
			ps.add((PeriodAssignationBean)p);
		}
		return ps;
	}

	private List<PeriodAssignation> toPeriodAssignationList(List<PeriodAssignationBean> l) {
		List<PeriodAssignation> ps = new ArrayList<PeriodAssignation>();
		for (PeriodAssignation p : l) {
			ps.add(p);
		}
		return ps;
	}
	
	
	@Override
	public String toJson(List<PeriodAssignation> o) {
		PeriodAssignationListContainer sc = new PeriodAssignationListContainer();
		sc.list = toPeriodAssignationBeanList(o);
		String d = WRITER.toJson(sc);
		logger.log(Level.WARNING,"piriti : " + d);
		return d;
	}

	@Override
	public List<PeriodAssignation> read(String json) {
		logger.log(Level.WARNING,"piriti : " + json);
		PeriodAssignationListContainer sc = READER.read(json);
		return toPeriodAssignationList(sc.list);
	}
}
