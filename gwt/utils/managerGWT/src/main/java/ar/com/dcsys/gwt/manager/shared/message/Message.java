package ar.com.dcsys.gwt.manager.shared.message;

import java.util.List;

public interface Message {

	public String getFunction();
	public void setFunction(String function);
	public List<String> getParams();
	public void setParams(List<String> params);
	
}
