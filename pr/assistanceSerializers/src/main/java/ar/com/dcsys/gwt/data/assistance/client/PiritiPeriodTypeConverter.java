package ar.com.dcsys.gwt.data.assistance.client;

import java.util.HashSet;
import java.util.Set;

import name.pehl.piriti.converter.client.Converter;
import ar.com.dcsys.data.common.Days;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.period.PeriodTypeDailyParams;
import ar.com.dcsys.data.period.PeriodTypeNull;
import ar.com.dcsys.data.period.PeriodTypeSystem;
import ar.com.dcsys.data.period.PeriodTypeWatchman;

public class PiritiPeriodTypeConverter implements Converter<PeriodType> {
	
	@Override
	public PeriodType convert(String json) {
		if (json == null || json.equals("")) {
			return null;
		}
		
		json.replace("\"", "");
		String[] str = json.split(",");
		String type = (str[0].split(":"))[0]; 
		
		if (type.equals(PeriodTypeNull.class.getName())) {
			PeriodTypeNull p = new PeriodTypeNull();
			for(int i = 0; i <= str.length; i++) {
				String [] elems = str[i].split(":");
				if (elems[0].equals("id")) {
					p.setId(elems[1]);
					break;
				}
			}
			return p;
		}
		
		if (type.equals(PeriodTypeSystem.class.getName())) {
			PeriodTypeSystem p = new PeriodTypeSystem();
			for(int i = 0; i <= str.length; i++) {
				String [] elems = str[i].split(":");
				if (elems[0].equals("id")) {
					p.setId(elems[1]);
					break;
				}
			}
			return p;
		}
		
		if (type.equals(PeriodTypeWatchman.class.getName())) {
			PeriodTypeWatchman p = new PeriodTypeWatchman();
			for(int i = 0; i <= str.length; i++) {
				String [] elems = str[i].split(":");
				if (elems[0].equals("id")) {
					p.setId(elems[1]);
					break;
				}
			}
			return p;
		}
		
		if (type.equals(PeriodTypeDailyParams.class.getName())) {
			PeriodTypeDailyParams p = new PeriodTypeDailyParams();
			for(int i = 0; i <= str.length; i++) {
				String [] elems = str[i].split(":");
				
				if (elems[0].equals("id")) {
					p.setId(elems[1]);
				} else {
					Set<Days> days = new HashSet<>();
					for(Days day : Days.values()) {
						if(elems[0].equals(day.toString())) {
							if (elems[1].equals("true")) {
								days.add(day);
							}
						}
					}
					p.setDays(days);
				}
			}
			return p;
		}
		
		return null;
	}
	
	@Override
	public String serialize(PeriodType value) {
		String d = "";
		if (value instanceof PeriodTypeNull) {
			PeriodTypeNull p = (PeriodTypeNull)value;
			d = "{\"type\":\"" + PeriodTypeNull.class.getName() + "\",\"id\":\"" + p.getId() + "\",\"name\":\"" + p.getName() + "\"}";
			return d;
		}
		if (value instanceof PeriodTypeSystem) {
			PeriodTypeSystem p = (PeriodTypeSystem)value;
			d = "{\"type\":\"" + PeriodTypeSystem.class.getName() + "\",\"id\":\"" + p.getId() + "\",\"name\":\"" + p.getName() + "\"}";
			return d;
		}
		if (value instanceof PeriodTypeWatchman) {
			PeriodTypeWatchman p = (PeriodTypeWatchman)value;
			d = "{\"type\":\"" + PeriodTypeWatchman.class.getName() + "\",\"id\":\"" + p.getId() + "\",\"name\":\"" + p.getName() + "\"}";
			return d;
		}
		if (value instanceof PeriodTypeDailyParams) {
			PeriodTypeDailyParams p = (PeriodTypeDailyParams)value;
			d = "{\"type\":\"" + PeriodTypeSystem.class.getName() + "\",\"id\":\"" + p.getId() + "\",\"name\":\"" + p.getName() + "\"";
			for(Days day : Days.values()) {
				d += ",\"" + day.toString() + "\":";
				if (p.getDays().contains(day)) {
					d += "true";				
				} else {
					d += "false";
				}
			}
			d += "}";
			return d;
		}

		return "";
	}

}
