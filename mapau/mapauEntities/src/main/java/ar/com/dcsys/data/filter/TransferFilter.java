package ar.com.dcsys.data.filter;

import java.io.Serializable;

public class TransferFilter implements Serializable {

	private static final long serialVersionUID = 1L;
	private TransferFilterType type;
	private String param;
	
	public TransferFilter() {
	}
	
	public TransferFilter(TransferFilterType type, String param) {
		this.type = type;
		this.param = param;
	}

	public TransferFilterType getType() {
		return type;
	}

	public void setType(TransferFilterType type) {
		this.type = type;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
	
	
}
