package ar.com.dcsys.gwt.assistance.client.manager;

import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.shared.PersonValueProxy;

public interface PeriodsManager {
	
	public void findIdsPersonsWithPeriodAssignation(Receiver<PersonValueProxy> receiver);

}
