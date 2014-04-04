package ar.com.dcsys.data.document;

import java.util.Date;

public interface Document {

	public Date getCreated();
	public String getName();
	public String getDescription();
	public String getMimeType();
	public String getEncoding();
	public byte[] getContent();
	
}
