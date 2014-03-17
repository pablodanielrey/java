package ar.com.dcsys.gwt.assistance.client.ui.auth;

import com.google.gwt.core.client.GWT;
import ar.com.dcsys.gwt.assistance.client.gin.ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector;

public class ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector_fragment {
  public void memberInject_Key$type$ar$com$dcsys$gwt$assistance$client$ui$auth$PinAuthData$_annotation$$none$$(ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData injectee) {
    
  }
  
  private ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthDataView singleton_Key$type$ar$com$dcsys$gwt$assistance$client$ui$auth$PinAuthDataView$_annotation$$none$$ = null;
  
  public ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthDataView get_Key$type$ar$com$dcsys$gwt$assistance$client$ui$auth$PinAuthDataView$_annotation$$none$$() {
    
    if (singleton_Key$type$ar$com$dcsys$gwt$assistance$client$ui$auth$PinAuthDataView$_annotation$$none$$ == null) {
    ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthDataView result = get_Key$type$ar$com$dcsys$gwt$assistance$client$ui$auth$PinAuthData$_annotation$$none$$();
        singleton_Key$type$ar$com$dcsys$gwt$assistance$client$ui$auth$PinAuthDataView$_annotation$$none$$ = result;
    }
    return singleton_Key$type$ar$com$dcsys$gwt$assistance$client$ui$auth$PinAuthDataView$_annotation$$none$$;
    
  }
  
  
  /**
   * Binding for ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData declared at:
   *   Implicit GWT.create binding for ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData
   */
  public ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData get_Key$type$ar$com$dcsys$gwt$assistance$client$ui$auth$PinAuthData$_annotation$$none$$() {
    Object created = GWT.create(ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData.class);
    assert created instanceof ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData;
    ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData result = (ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData) created;
    
    memberInject_Key$type$ar$com$dcsys$gwt$assistance$client$ui$auth$PinAuthData$_annotation$$none$$(result);
    
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
