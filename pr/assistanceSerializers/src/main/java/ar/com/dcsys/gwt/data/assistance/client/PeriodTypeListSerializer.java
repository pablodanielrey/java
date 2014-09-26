package ar.com.dcsys.gwt.data.assistance.client;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.pr.CSD;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class PeriodTypeListSerializer implements CSD<List<PeriodType>> {

	private static final Logger logger = Logger.getLogger(PeriodTypeListSerializer.class.getName());
	
	
	@Override
	public String toJson(List<PeriodType> o) {
		if (o == null) {
			return "";
		}
		
		JSONArray typesObj = new JSONArray();
		
		for (int i=0; i<o.size(); i++) {
			typesObj.set(i,PeriodTypeUtilsSerializers.toJson(o.get(i)));
		}
		
		logger.log(Level.WARNING, "PeriodTypeListSerializer.toJson : " + typesObj.toString());
		return typesObj.toString();
	}

	@Override
	public List<PeriodType> read(String json) {
		logger.log(Level.WARNING,"PeriodTypeListSerializer : " + json);
		try {
			JSONValue value = JSONParser.parseStrict(json);
			JSONArray typesArray = value.isArray();
			
			if (typesArray == null) {
				return null;
			}
			
			List<PeriodType> types = new ArrayList<PeriodType>();
			for(int i=0; i<typesArray.size(); i++) {
				JSONObject typeObj = typesArray.get(i).isObject();
				PeriodType t = PeriodTypeUtilsSerializers.read(typeObj);
				types.add(t);
			}
			
			return types;
			
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
			return null;
		}
	}

}
