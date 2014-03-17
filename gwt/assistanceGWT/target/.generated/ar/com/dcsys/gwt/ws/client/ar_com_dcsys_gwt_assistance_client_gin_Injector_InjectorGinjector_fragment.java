package ar.com.dcsys.gwt.ws.client;

import com.google.gwt.core.client.GWT;
import ar.com.dcsys.gwt.assistance.client.gin.ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector;

public class ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector_fragment {
  public void memberInject_Key$type$ar$com$dcsys$gwt$ws$client$WebSocketImpl$_annotation$$none$$(ar.com.dcsys.gwt.ws.client.WebSocketImpl injectee) {
    
  }
  
  private ar.com.dcsys.gwt.ws.client.WebSocket singleton_Key$type$ar$com$dcsys$gwt$ws$client$WebSocket$_annotation$$none$$ = null;
  
  public ar.com.dcsys.gwt.ws.client.WebSocket get_Key$type$ar$com$dcsys$gwt$ws$client$WebSocket$_annotation$$none$$() {
    
    if (singleton_Key$type$ar$com$dcsys$gwt$ws$client$WebSocket$_annotation$$none$$ == null) {
    ar.com.dcsys.gwt.ws.client.WebSocket result = get_Key$type$ar$com$dcsys$gwt$ws$client$WebSocketImpl$_annotation$$none$$();
        singleton_Key$type$ar$com$dcsys$gwt$ws$client$WebSocket$_annotation$$none$$ = result;
    }
    return singleton_Key$type$ar$com$dcsys$gwt$ws$client$WebSocket$_annotation$$none$$;
    
  }
  
  
  /**
   * Binding for ar.com.dcsys.gwt.ws.client.WebSocketImpl declared at:
   *   Implicit binding for ar.com.dcsys.gwt.ws.client.WebSocketImpl
   */
  public ar.com.dcsys.gwt.ws.client.WebSocketImpl get_Key$type$ar$com$dcsys$gwt$ws$client$WebSocketImpl$_annotation$$none$$() {
    ar.com.dcsys.gwt.ws.client.WebSocketImpl result = ar$com$dcsys$gwt$ws$client$WebSocketImpl_ar$com$dcsys$gwt$ws$client$WebSocketImpl_methodInjection(injector.getFragment_com_google_gwt_event_shared().get_Key$type$com$google$gwt$event$shared$EventBus$_annotation$$none$$(), injector.getFragment_ar_com_dcsys_gwt_message_shared().get_Key$type$ar$com$dcsys$gwt$message$shared$MessageEncoderDecoder$_annotation$$none$$());
    memberInject_Key$type$ar$com$dcsys$gwt$ws$client$WebSocketImpl$_annotation$$none$$(result);
    
    return result;
    
  }
  
  public ar.com.dcsys.gwt.ws.client.WebSocketImpl ar$com$dcsys$gwt$ws$client$WebSocketImpl_ar$com$dcsys$gwt$ws$client$WebSocketImpl_methodInjection(com.google.gwt.event.shared.EventBus _0, ar.com.dcsys.gwt.message.shared.MessageEncoderDecoder _1) {
    return new ar.com.dcsys.gwt.ws.client.WebSocketImpl(_0, _1);
  }
  
  
  /**
   * Field for the enclosing injector.
   */
  private final ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector injector;
  public ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector_fragment(ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector injector) {
    this.injector = injector;
  }
  
}
