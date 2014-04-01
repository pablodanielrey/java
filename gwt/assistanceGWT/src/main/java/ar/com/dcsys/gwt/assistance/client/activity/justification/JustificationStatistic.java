package ar.com.dcsys.gwt.assistance.client.activity.justification;

public class JustificationStatistic {
	private String name;
	private int count;
	private String description;
	
	public JustificationStatistic (String name, String description) {
		this.setName(name);
		this.setDescription(description);
		this.setCount(0);
	}
	
	public void incrementCount() {
		this.count++;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}	
}
