package ar.com.dcsys.gwt.data.assistance.client;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class PeriodAssignationListSerializer implements CSD<List<PeriodAssignation>> {



	private static final Logger logger = Logger.getLogger(PeriodAssignationListSerializer.class.getName());

	@Override
	public String toJson(List<PeriodAssignation> o) {
		if (o == null) {
			return "";
		}
		
		JSONArray periodsObj = new JSONArray();
		
		for (int i=0; i<o.size(); i++) {
			periodsObj.set(i, PeriodAssignationUtilsSerializers.toJson(o.get(i)));
		}
		
		logger.log(Level.WARNING, "PeriodAssignationListSerializer.toJson : " + periodsObj.toString());
		return periodsObj.toString();
	}
	
	@Override
	public List<PeriodAssignation> read(String json) {
		logger.log(Level.WARNING,"PeriodAssignationListSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONObject obj = value.isObject();
			JSONArray paArray = obj.get("list").isArray();
			
			if (paArray == null) {
				return null;
			}
			
			List<PeriodAssignation> periods = new ArrayList<PeriodAssignation>();
			for (int i=0; i<paArray.size(); i++) {
				JSONObject paObject = paArray.get(i).isObject();
				PeriodAssignation pa = PeriodAssignationUtilsSerializers.read(paObject);
				periods.add(pa);
			}
			
			return periods;
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}
}
