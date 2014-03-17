package ar.com.dcsys.gwt.assistance.client.gin;

import com.google.gwt.core.client.GWT;
import ar.com.dcsys.gwt.assistance.client.gin.ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector;

public class ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector_fragment {
  
  /**
   * Binding for ar.com.dcsys.gwt.assistance.client.gin.AssistedInjectionFactory declared at:
   *   ar.com.dcsys.gwt.assistance.client.gin.AssistanceGWTGinModule.configure(AssistanceGWTGinModule.java:25)
   */
  public ar.com.dcsys.gwt.assistance.client.gin.AssistedInjectionFactory get_Key$type$ar$com$dcsys$gwt$assistance$client$gin$AssistedInjectionFactory$_annotation$$none$$() {
    ar.com.dcsys.gwt.assistance.client.gin.AssistedInjectionFactory result = new ar.com.dcsys.gwt.assistance.client.gin.AssistedInjectionFactory() {
    
        public ar.com.dcsys.gwt.assistance.client.activity.auth.PinAuthDataActivity pinAuthDataActivity(ar.com.dcsys.gwt.assistance.client.place.PinAuthDataPlace _0) {
          return injector.getFragment_ar_com_dcsys_gwt_assistance_client_activity_auth().assistedInject_pinAuthDataActivityKey$type$ar$com$dcsys$gwt$assistance$client$gin$AssistedInjectionFactory$_annotation$$none$$(_0);
        }
    };
    return result;
    
  }
  
  
  /**
   * Field for the enclosing injector.
   */
  private final ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector injector;
  public ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector_fragment(ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector injector) {
    this.injector = injector;
  }
  
}
