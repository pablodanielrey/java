package ar.com.dcsys.gwt.mapau.client.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment.State;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterValue;

import com.google.inject.Inject;

public class AppointmentsManagerBean implements AppointmentsManager {

	
	private final List<MapauAppointment> original = new ArrayList<MapauAppointment>();
	private final List<MapauAppointment> working = new ArrayList<MapauAppointment>();
	private final MapauManager rf;
	
	private final DeleteTransaction deleteTransaction;
	private final CreateNewTransaction createNewTransaction;
	private final ModifyTransaction modifyTransaction;	
	private final FilterActivityUtils filterActivityUtils;
	
	@Inject
	public AppointmentsManagerBean(MapauManager rf, FilterActivityUtils filterActivityUtils,
								   DeleteTransaction deleteTransaction,
								   CreateNewTransaction createNewTransaction,
								   ModifyTransaction modifyTransaction) {
		this.rf = rf;
		this.filterActivityUtils = filterActivityUtils;
		this.deleteTransaction = deleteTransaction;
		this.createNewTransaction = createNewTransaction;
		this.modifyTransaction = modifyTransaction;
	}
	
	
	private List<MapauAppointment> wrapAppointments(List<AppointmentV2> apps) {
		List<MapauAppointment> mapps = new ArrayList<MapauAppointment>();
		for (AppointmentV2 av2 : apps) {
			MapauAppointment app = new MapauAppointment(av2);
			mapps.add(app);
		}
		return mapps;
	}
	
	
	@Override
	public void findByFilters(List<FilterValue<?>> filters,	final Receiver<List<MapauAppointment>> receiver) {
		
		filterActivityUtils.findAppointmentsV2(filters, new Receiver<List<AppointmentV2>>() {
			@Override
			public void onSuccess(List<AppointmentV2> apps) {
				original.clear();
				
				List<MapauAppointment> toReturn = new ArrayList<MapauAppointment>();
				toReturn.addAll(working);
				
				if (apps == null || apps.size() <= 0) {
					receiver.onSuccess(toReturn);
				}
				
				List<MapauAppointment> mapps = wrapAppointments(apps);
				original.addAll(mapps);
				
				Iterator<MapauAppointment> it = mapps.iterator();
				while (it.hasNext()) {
					String id = it.next().getId();
					for (MapauAppointment tr : toReturn) {
						if (id.equals(tr.getId())) {
							it.remove();
						}
					}
				}
				toReturn.addAll(mapps);
				
				receiver.onSuccess(toReturn);
			}
			@Override
			public void onFailure(Throwable error) {
				receiver.onFailure(error);
			}
		});
	}
	
	@Override
	public void findAllAppointmentsInClient(Receiver<List<MapauAppointment>> receiver) {
		
		List<MapauAppointment> mapps = new ArrayList<MapauAppointment>(original);
		Iterator<MapauAppointment> it = mapps.iterator();
		while (it.hasNext()) {
			String id = it.next().getId();
			for (MapauAppointment ma : working) {
				if (id.equals(ma.getId())) {
					it.remove();
				}
			}
		}
		mapps.addAll(working);
		
		receiver.onSuccess(mapps);
	}
	
	
	private Date getDate(Date original, int hours, int minutes, int seconds) {
		Date date = new Date(original.getTime());
		date.setHours(hours);
		date.setMinutes(minutes);
		date.setSeconds(seconds);
		return date;
	}
	
	public List<MapauAppointment> findAllAppointmentsInWorkingBy(MapauAppointment app, List<Date> dates, boolean checkHour) {
		
		List<MapauAppointment> related = new ArrayList<MapauAppointment>();
		
		Date start = app.getStart();
		int shour = start.getHours();
		int sminute = start.getMinutes();
		int ssecond = start.getSeconds();
		
		Date end = app.getEnd();
		int ehour = end.getHours();
		int eminute = end.getMinutes();
		int esecond = end.getSeconds();
		
		for (Date date : dates) {
			for (MapauAppointment wapp : working) {
				Date wstart = wapp.getStart();
				Date wend = wapp.getEnd();

				if (checkHour) {
					Date csdate = getDate(date,shour,sminute,ssecond);
					Date cedate = getDate(date,ehour,eminute,esecond);
					if (csdate.equals(wstart) && cedate.equals(wend)) {
						related.add(wapp);
					}
				} else {
					Date csdate = getDate(date,0,0,0);
					Date cedate = getDate(date,23,59,59);
					if (!(csdate.after(wstart) || end.after(cedate))) {
						related.add(wapp);
					}
				}
			}
		}
		
		return related;
	}
	

	@Override
	public void findAllAppointmentsBy(final MapauAppointment app, final List<Date> dates, final boolean checkHour, final Receiver<List<MapauAppointment>> receiver) {
		
		
		if (isServer(app)) {
			
			AppointmentV2 appv2 = app.getAppointment();
			rf.findAllAppointmentsBy(appv2, dates, checkHour, new Receiver<List<AppointmentV2>>() {
				@Override
				public void onSuccess(List<AppointmentV2> appsV2) {
					
					List<MapauAppointment> mapps = wrapAppointments(appsV2);
					
					// reemplazo en la lista de originales.
					for (MapauAppointment newApp : mapps) {
						String id = newApp.getId();
						
						Iterator<MapauAppointment> it = original.iterator();
						while (it.hasNext()) {
							MapauAppointment originalAppointment = it.next();
							if (id.equals(originalAppointment.getId())) {
								it.remove();
							}
						}
					}
					
					original.addAll(mapps);
					
					
					/////// elimino todos los originales retornados por el servidor que están en working ////////////
					
					Iterator<MapauAppointment> it = mapps.iterator();
					while (it.hasNext()) {
						String id = it.next().getId();
						for (MapauAppointment wapp : working) {
							if (id.equals(wapp.getId())) {
								it.remove();
								break;
							}
						}
					}
					
					
					/////// agrego los que sean relacionados en working ////////////////
					
					List<MapauAppointment> related = findAllAppointmentsInWorkingBy(app,dates,checkHour);
					mapps.addAll(related);
					
					
					receiver.onSuccess(mapps);
					
				}
				@Override
				public void onFailure(Throwable error) {
					receiver.onFailure(error);
				}
			});
			
		} else {
			
			List<MapauAppointment> related = findAllAppointmentsInWorkingBy(app,dates,checkHour);
			receiver.onSuccess(related);
			
		}
		
	}
	
	@Override
	public boolean replaceInOriginal(MapauAppointment replace) {
		String id = replace.getId();
		
		boolean found = false;
		Iterator<MapauAppointment> it = original.iterator();
		while (it.hasNext()) {
			if (id.equals(it.next().getId())) {
				it.remove();
				found = true;
			}
		}
		if (found) {
			original.add(replace);
		}
		return found;
	}
	
	@Override
	public boolean replaceInWorking(MapauAppointment replace) {
		String id = replace.getId();
		
		boolean found = false;
		Iterator<MapauAppointment> it = working.iterator();
		while (it.hasNext()) {
			if (id.equals(it.next().getId())) {
				it.remove();
				found = true;
			}
		}
		if (found) {
			working.add(replace);
		}
		return found;
	}
	
	

	@Override
	public boolean replace(MapauAppointment replace) {
		boolean found = false;
		found = replaceInOriginal(replace);
		found = replaceInWorking(replace) || found;
		return found;
	}

	@Override
	public void add(MapauAppointment app) {
		if (!replaceInWorking(app)) {
			working.add(app);
		}
	}
	
	@Override
	public void remove(MapauAppointment app) {
		String id = app.getId();
		
		boolean found = false;
		Iterator<MapauAppointment> it = working.iterator();
		while (it.hasNext()) {
			if (id.equals(it.next().getId())) {
				it.remove();
			}
		}
	}

	@Override
	public void commit(final Receiver<Void> receiver) {

		final Map<State,List<MapauAppointment>> apps = AppointmentsUtils.clasifyAppointments(working);

		original.clear();
		working.clear();
		
		deleteWorkingAppointments(apps, new Receiver<List<String>>() {
			@Override
			public void onSuccess(List<String> ids) {

				modifyWorkingAppointments(apps, new Receiver<Void>() {
					
					@Override
					public void onSuccess(Void arg0) {

						createWorkingAppointments(apps, new Receiver<Void>() {
							@Override
							public void onSuccess(Void arg0) {
								receiver.onSuccess(arg0);
							}
							@Override
							public void onFailure(Throwable error) {
								receiver.onFailure(error);
							}
						});
						
					}
					
					@Override
					public void onFailure(Throwable error) {
						receiver.onFailure(error);
					}
					
				});
			}
			@Override
			public void onFailure(Throwable error) {
				receiver.onFailure(error);
			}
		});		
		
	}

	@Override
	public void cancel() {
		working.clear();
	}

	@Override
	public boolean isWorking(MapauAppointment app) {
		String id = app.getId();
		for (MapauAppointment ma : working) {
			if (id.equals(ma.getId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isServer(MapauAppointment app) {
		String id = app.getId();
		for (MapauAppointment ma : original) {
			if (id.equals(ma.getId())) {
				return true;
			}
		}
		return false;
	}

	
	////////////
	


	
	private void deleteWorkingAppointments(Map<State,List<MapauAppointment>> apps, final Receiver<List<String>> rec) {

		final List<String> ids = new ArrayList<String>();
		List<MapauAppointment> toDelete = apps.get(State.DELETED);
		
		for (MapauAppointment d : toDelete) {
			ids.add(d.getId());
		}
		
		deleteTransaction.delete(toDelete, new Receiver<Void>() {
			@Override
			public void onSuccess(Void v) {
				rec.onSuccess(ids);
			}
			@Override
			public void onFailure(Throwable error) {
				rec.onFailure(error);
			}
		});
		
	}
	
	
	private void createWorkingAppointments(Map<State,List<MapauAppointment>> apps, final Receiver<Void> rec) {

		List<MapauAppointment> newA = apps.get(State.NEW);
		createNewTransaction.createNew(newA, new Receiver<Void>() {
			@Override
			public void onSuccess(Void arg0) {
				rec.onSuccess(arg0);
			}
			
			@Override
			public void onFailure(Throwable error) {
				rec.onFailure(error);
			}
			
		});
		
	}
	
	private void modifyWorkingAppointments(Map<State,List<MapauAppointment>> apps, final Receiver<Void> rec) {

		List<MapauAppointment> toModify = apps.get(State.MODIFIED);
		modifyTransaction.modify(toModify, new Receiver<Void>() {
			@Override
			public void onSuccess(Void v) {
				rec.onSuccess(v);
			}
			@Override
			public void onFailure(Throwable error) {
				rec.onFailure(error);
			}
		});		
		
		
	}
	
	
}
