package ar.com.dcsys.gwt.mapau.client.manager;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.data.appointment.AppointmentV2Bean;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.inject.Inject;


public class CreateNewTransaction {

	private final MapauManager rf;
	List<MapauAppointment> newA;
	
	@Inject
	public CreateNewTransaction(MapauManager rf) {
		this.rf = rf;
	}
	
	public void createNew(List<MapauAppointment> ma, Receiver<Void> rec) {
		this.newA = ma;
		createNewRecursive(rec);
	}
	
	private void createNewRecursive(final Receiver<Void> rec) {
		
		if (newA.size() <= 0) {
			rec.onSuccess(null);
			return;
		}
		
		// selecciono y saco de newA los del mismo curso.
		List<MapauAppointment> sameCourse = new ArrayList<MapauAppointment>();
		MapauAppointment first = newA.get(0);
		sameCourse.add(first);
		newA.remove(first);
		Course c = first.getCourse();
		for (MapauAppointment ma : newA) {
			if (c.getId().equals(ma.getCourse().getId())) {
				sameCourse.add(ma);
			}
		}
		newA.removeAll(sameCourse);

		if (sameCourse.size() <= 0) {
			
			rec.onSuccess(null);
			return;
			
		} else {
			List<AppointmentV2> newAv2 = new ArrayList<AppointmentV2>();
			
			for (MapauAppointment ma : sameCourse) {
				AppointmentV2 app = new AppointmentV2Bean();
				ma.to(app);
				newAv2.add(app);
			}
			rf.createNewAppointments(newAv2, new Receiver<Void>() {
				@Override
				public void onSuccess(Void arg0) {
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						@Override
						public void execute() {
							if (newA.size() <= 0) {
								rec.onSuccess(null);
							} else {
								createNewRecursive(rec);
							}
						}
					});
				}
				@Override
				public void onFailure(Throwable error) {
					rec.onFailure(error);
				}
			});
		} 
		
	}	
	
}
