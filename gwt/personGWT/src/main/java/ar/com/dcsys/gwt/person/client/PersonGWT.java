package ar.com.dcsys.gwt.person.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PersonGWT implements EntryPoint {
 

  private final Messages messages = GWT.create(Messages.class);

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
	  Window.alert(messages.helloWorldFirst());
	  Window.alert(messages.helloWorldLast());
  }
}
