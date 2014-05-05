package ar.com.dcsys.gwt.mapau.shared.data.reserve;

import ar.com.dcsys.data.filter.TransferFilterType;

public interface TransferFilter {

	public String getParam();
	public void setParam(String param);
	
	public TransferFilterType getType();
	public void setType(TransferFilterType type);
	
}
