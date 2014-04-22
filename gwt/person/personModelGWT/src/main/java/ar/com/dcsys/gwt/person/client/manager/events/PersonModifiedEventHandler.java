package ar.com.dcsys.gwt.person.client.manager.events;

import com.google.gwt.event.shared.EventHandler;

public interface PersonModifiedEventHandler extends EventHandler {

	public void onPersonModified(PersonModifiedEvent event);
	
}
