package ar.com.dcsys.data.filter;


public interface TransferFilter {

	public TransferFilterType getType();
	public void setType(TransferFilterType type);

	public String getParam();
	public void setParam(String param);
	
	
}
