package ar.com.dcsys.gwt.person.client.manager.events;

import com.google.gwt.event.shared.GwtEvent;

public class PersonModifiedEvent extends GwtEvent<PersonModifiedEventHandler> {

	public static final GwtEvent.Type<PersonModifiedEventHandler> TYPE = new GwtEvent.Type<PersonModifiedEventHandler>();
	
	private final String id;
	
	public PersonModifiedEvent(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	@Override
	public GwtEvent.Type<PersonModifiedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PersonModifiedEventHandler handler) {
		handler.onPersonModified(this);
	}
 
}
