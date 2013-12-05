package ar.com.dcsys.gwt.mapau.client.manager;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterValue;



public interface AppointmentsManager {

	public void findByFilters(List<FilterValue<?>> filters, Receiver<List<MapauAppointment>> receiver);
	
	public void findAllAppointmentsBy(MapauAppointment app, List<Date> dates, boolean checkHour, Receiver<List<MapauAppointment>> receiver);
	public void findAllAppointmentsInClient(Receiver<List<MapauAppointment>> receiver);
	
	public boolean replaceInWorking(MapauAppointment replace);
	public boolean replaceInOriginal(MapauAppointment replace);
	public boolean replace(MapauAppointment replace);
	
	public void add(MapauAppointment app);
	public void remove(MapauAppointment app);
	
	public void commit(Receiver<Void> receiver);
	public void cancel();
	
	public boolean isWorking(MapauAppointment app);
	public boolean isServer(MapauAppointment app);
	
}
