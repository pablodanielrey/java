package ar.com.dcsys.gwt.assistance.client.gin;

public class ar_com_dcsys_gwt_assistance_client_gin_InjectorImpl implements ar.com.dcsys.gwt.assistance.client.gin.Injector {
  
  /**
   * Top-level injector instance for injector interface ar.com.dcsys.gwt.assistance.client.gin.Injector.
   */
  private final ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector fieldar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector = new ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector(this);
  public ar_com_dcsys_gwt_assistance_client_gin_InjectorImpl() {
    
  }
  
  public ar.com.dcsys.gwt.assistance.client.activity.AssistanceActivityMapper assistanceActivityMapper() {
    return fieldar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector.getFragment_ar_com_dcsys_gwt_assistance_client_activity().get_Key$type$ar$com$dcsys$gwt$assistance$client$activity$AssistanceActivityMapper$_annotation$$none$$();
  }
  
  public com.google.gwt.event.shared.EventBus eventbus() {
    return fieldar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector.getFragment_com_google_gwt_event_shared().get_Key$type$com$google$gwt$event$shared$EventBus$_annotation$$none$$();
  }
  
  public ar.com.dcsys.gwt.assistance.client.gin.AssistedInjectionFactory factory() {
    return fieldar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector.getFragment_ar_com_dcsys_gwt_assistance_client_gin().get_Key$type$ar$com$dcsys$gwt$assistance$client$gin$AssistedInjectionFactory$_annotation$$none$$();
  }
  
  public ar.com.dcsys.gwt.ws.client.WebSocket ws() {
    return fieldar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector.getFragment_ar_com_dcsys_gwt_ws_client().get_Key$type$ar$com$dcsys$gwt$ws$client$WebSocket$_annotation$$none$$();
  }
  
}
