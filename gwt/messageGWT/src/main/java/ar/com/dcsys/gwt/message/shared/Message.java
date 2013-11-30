package ar.com.dcsys.gwt.message.shared;


public interface Message {

	public void setSessionId(String id);
	public String getSessionId();
	
	public void setId(String id);
	public String getId();
	
	public void setType(MessageType type);
	public MessageType getType();

	public void setPayload(String p);
	public String getPayload();
}
