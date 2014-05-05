package ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter;

/**
 * Parche para agregarle un Id al Attendee que la version de gwt-cal no lo tiene.
 * 
 * @author pablo
 *
 */

public class Attendee extends com.bradrydzewski.gwt.calendar.client.Attendee {

	private static final long serialVersionUID = 1L;
	
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
