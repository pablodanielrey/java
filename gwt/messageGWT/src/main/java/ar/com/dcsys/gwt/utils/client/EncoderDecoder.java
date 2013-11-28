package ar.com.dcsys.gwt.utils.client;

public class EncoderDecoder {

	private static native String b64encode(String a) /*-{
	  return window.btoa(a);
	}-*/;
	
	private static native String b64decode(String a) /*-{
	  return window.atob(a);
	}-*/;
	
	
}
