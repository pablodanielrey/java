package ar.com.dcsys.gwt.assistance.client.ui.period;

public enum PERIODFILTER {
	ALL("Todos"),WORKING("Días Laborables"),ABSENT("Ausencias");
	
	private String description;
	
	private PERIODFILTER(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
