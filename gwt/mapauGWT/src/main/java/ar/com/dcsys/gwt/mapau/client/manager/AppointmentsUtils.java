package ar.com.dcsys.gwt.mapau.client.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment.State;

public class AppointmentsUtils {

	public static Map<State,List<MapauAppointment>> clasifyAppointments(List<MapauAppointment> working) {
		
		Map<State,List<MapauAppointment>> data = new HashMap<State,List<MapauAppointment>>();
		
		for (State s : State.values()) {
			data.put(s,new ArrayList<MapauAppointment>());
		}
		
		for (MapauAppointment ma : working) {
			data.get(ma.getState()).add(ma);
		}
		
		return data;		
	}
	
}
