package ar.com.dcsys.data.period;

import java.io.Serializable;

public enum PeriodType implements Serializable {
	NULL("Nulo"), DAILY("Diario"), SYSTEMS("Sistemas"), WATCHMAN("Sereno");
	
	private String description;
	
	private PeriodType() {
		//por ser serializable
	}
	
	private PeriodType(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
