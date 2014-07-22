package ar.com.dcsys.gwt.messages.shared;

public class TransportEvent {

	private String type;
	private String message;
	private TransportReceiver transportReceiver;

	public TransportReceiver getTransportReceiver() {
		return transportReceiver;
	}

	public void setTransportReceiver(TransportReceiver transportReceiver) {
		this.transportReceiver = transportReceiver;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
