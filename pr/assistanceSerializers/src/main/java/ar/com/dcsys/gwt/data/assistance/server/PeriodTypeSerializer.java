package ar.com.dcsys.gwt.data.assistance.server;

import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.pr.CSD;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PeriodTypeSerializer implements CSD<PeriodType> {
	
	private static final Gson gson = (new GsonBuilder()).create();
	
	@Override
	public PeriodType read(String json) {
		PeriodType pt = gson.fromJson(json, PeriodType.class);
		return pt;
	}
	
	@Override
	public String toJson(PeriodType o) {
		String data = gson.toJson(o);
		return data;
	}
	
}
