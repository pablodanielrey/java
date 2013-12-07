package ar.com.dcsys.gwt.mapau.client.manager;

import java.util.List;

import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;

import com.google.inject.Inject;


public class ModifyTransaction {

	private final MapauManager rf;
	
	List<MapauAppointment> modify;
	
	@Inject
	public ModifyTransaction(MapauManager rf) {
		this.rf = rf;
	}
	
	
	public void modify(List<MapauAppointment> ma, final Receiver<Void> rec) {
		this.modify = ma;
		modifyRecursive(rec);
	}
	
	private void modifyRecursive(final Receiver<Void> rec) {
		
		if (modify.size() <= 0) {
			rec.onSuccess(null);
			return;
		}
		
		MapauAppointment toModify = modify.get(0);
		modify.remove(toModify);
		AppointmentV2 app = toModify.getAppointment();

		rf.modifyAppointment(app, new Receiver<Void>() {
			@Override
			public void onSuccess(Void arg0) {
				
				if (modify.size() <= 0) {
					rec.onSuccess(null);
				} else {
					modifyRecursive(rec);
				}
				
			}
			@Override
			public void onFailure(Throwable error) {
				rec.onFailure(error);
			}
		});
	}	
	
}
