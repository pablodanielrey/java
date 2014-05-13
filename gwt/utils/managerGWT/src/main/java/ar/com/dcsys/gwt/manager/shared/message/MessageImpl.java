package ar.com.dcsys.gwt.manager.shared.message;

import java.util.List;

public class MessageImpl implements Message {

	private String function;
	private List<String> params;
	
	@Override
	public String getFunction() {
		return function;
	}
	
	@Override
	public void setFunction(String function) {
		this.function = function;
	}
	
	@Override
	public List<String> getParams() {
		return params;
	}
	
	@Override
	public void setParams(List<String> params) {
		this.params = params;
	}
	
}
