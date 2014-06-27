package ar.com.dcsys.gwt.data.assistance.client;

import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.pr.CSD;

public class PeriodTypeSerializer implements CSD<PeriodType> {
	
	
	@Override
	public PeriodType read(String json) {
		String data = json.replace("\"", "");
		return PeriodType.valueOf(data);
	}
	
	@Override
	public String toJson(PeriodType o) {
		String data = "\"" + o.toString() + "\"";
		return data;
	}
	
}
