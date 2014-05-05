package ar.com.dcsys.gwt.manager.shared.message;

import java.util.List;

public class Message {

	private String function;
	private List<String> params;
	
	public String getFunction() {
		return function;
	}
	
	public void setFunction(String function) {
		this.function = function;
	}
	
	public List<String> getParams() {
		return params;
	}
	
	public void setParams(List<String> params) {
		this.params = params;
	}
	
}
