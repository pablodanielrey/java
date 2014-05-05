package ar.com.dcsys.gwt.mapau.client.manager;

import java.util.List;

import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;

import com.google.inject.Inject;

public class DeleteTransaction {

	private final MapauManager rf;
	
	List<MapauAppointment> delete;

	
	@Inject
	public DeleteTransaction(MapauManager rf) {
		this.rf = rf;
	}
	
	
	public void delete(List<MapauAppointment> ma, final Receiver<Void> rec) {
		this.delete = ma;
		deleteRecursive(rec);
	}
	
	private void deleteRecursive(final Receiver<Void> rec) {
		
		if (delete.size() <= 0) {
			rec.onSuccess(null);
			return;
		}
		
		MapauAppointment toDelete = delete.get(0);
		delete.remove(toDelete);
		AppointmentV2 app = toDelete.getAppointment();
		
		rf.deleteAppointment(app, new Receiver<Void>() {
			@Override
			public void onSuccess(Void arg0) {
				
				if (delete.size() <= 0) {
					rec.onSuccess(null);
				} else {
					deleteRecursive(rec);
				}
				
			}
			@Override
			public void onFailure(Throwable error) {
				rec.onFailure(error);
			}
		});
	}	
	
}
