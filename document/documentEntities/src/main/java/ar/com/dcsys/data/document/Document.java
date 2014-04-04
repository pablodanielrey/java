package ar.com.dcsys.data.document;

import java.util.Date;

public interface Document {

	public String getId();
	void setId(String id);
	
	public Date getCreated();
	public String getName();
	public String getDescription();
	public String getMimeType();
	public String getEncoding();
	public byte[] getContent();
	
}
