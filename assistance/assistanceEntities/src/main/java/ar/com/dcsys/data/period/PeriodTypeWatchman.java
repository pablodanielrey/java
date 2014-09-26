package ar.com.dcsys.data.period;

public class PeriodTypeWatchman implements PeriodType {
	
	private String id;
	private String type;
	
	public PeriodTypeWatchman() {
		type = PeriodTypeWatchman.class.getName();
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
		return "Sereno";
	}
	
	

}
