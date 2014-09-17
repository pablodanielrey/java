package ar.com.dcsys.data.period;

import java.util.HashSet;
import java.util.Set;

import ar.com.dcsys.data.common.Days;

public class PeriodTypeDailyParams implements PeriodType {

	private Set<Days> days;
	private String id;
	
	public PeriodTypeDailyParams() {
		days = new HashSet<Days>();
	}	
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return "Diario";
	}
		
	public Set<Days> getDays() {
		return days;
	}
	
	public void setDays(Set<Days> days) {
		this.days = days;
	}
	
	
	
}
