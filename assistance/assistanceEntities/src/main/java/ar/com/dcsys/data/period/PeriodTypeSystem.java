package ar.com.dcsys.data.period;

public class PeriodTypeSystem implements PeriodType {
	
	private String id;
	private String type;
	
	public PeriodTypeSystem() {
		type = PeriodTypeSystem.class.getName();
	}
	
	@Override
	public String getType() {
		return type;
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
		return "Sistemas";
	}
	
	

}
