/**
 * Ya que falta en la librería de websockets que estoy usando agrego de acuerdo a :
 * https://developer.mozilla.org/en-US/docs/Web/API/WebSocket#Ready_state_constants
 * el estado de websocket.
 */

package ar.com.dcsys.gwt.person.client.ws;


public enum WebSocketState {

	CONNECTING(0), OPEN(1), CLOSING(2), CLOSED(3);
	
	private int state;
	
	private WebSocketState() { }
	
	private WebSocketState(int i) {
		state = i;
	}
	
	public int getState() {
		return state;
	}
	
}
