package ar.com.dcsys.data.period;

public class PeriodTypeNull implements PeriodType {
	
	private String id;
	
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
		return "Nulo";
	}
	
	

}
