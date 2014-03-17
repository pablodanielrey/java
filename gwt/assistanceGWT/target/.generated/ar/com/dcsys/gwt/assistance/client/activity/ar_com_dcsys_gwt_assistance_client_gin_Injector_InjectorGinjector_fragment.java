package ar.com.dcsys.gwt.assistance.client.activity;

import com.google.gwt.core.client.GWT;
import ar.com.dcsys.gwt.assistance.client.gin.ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector;

public class ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector_fragment {
  public void memberInject_Key$type$ar$com$dcsys$gwt$assistance$client$activity$AssistanceActivityMapper$_annotation$$none$$(ar.com.dcsys.gwt.assistance.client.activity.AssistanceActivityMapper injectee) {
    
  }
  
  private ar.com.dcsys.gwt.assistance.client.activity.AssistanceActivityMapper singleton_Key$type$ar$com$dcsys$gwt$assistance$client$activity$AssistanceActivityMapper$_annotation$$none$$ = null;
  
  public ar.com.dcsys.gwt.assistance.client.activity.AssistanceActivityMapper get_Key$type$ar$com$dcsys$gwt$assistance$client$activity$AssistanceActivityMapper$_annotation$$none$$() {
    
    if (singleton_Key$type$ar$com$dcsys$gwt$assistance$client$activity$AssistanceActivityMapper$_annotation$$none$$ == null) {
    ar.com.dcsys.gwt.assistance.client.activity.AssistanceActivityMapper result = ar$com$dcsys$gwt$assistance$client$activity$AssistanceActivityMapper_ar$com$dcsys$gwt$assistance$client$activity$AssistanceActivityMapper_methodInjection(injector.getFragment_ar_com_dcsys_gwt_assistance_client_gin().get_Key$type$ar$com$dcsys$gwt$assistance$client$gin$AssistedInjectionFactory$_annotation$$none$$());
    memberInject_Key$type$ar$com$dcsys$gwt$assistance$client$activity$AssistanceActivityMapper$_annotation$$none$$(result);
    
        singleton_Key$type$ar$com$dcsys$gwt$assistance$client$activity$AssistanceActivityMapper$_annotation$$none$$ = result;
    }
    return singleton_Key$type$ar$com$dcsys$gwt$assistance$client$activity$AssistanceActivityMapper$_annotation$$none$$;
    
  }
  
  public ar.com.dcsys.gwt.assistance.client.activity.AssistanceActivityMapper ar$com$dcsys$gwt$assistance$client$activity$AssistanceActivityMapper_ar$com$dcsys$gwt$assistance$client$activity$AssistanceActivityMapper_methodInjection(ar.com.dcsys.gwt.assistance.client.gin.AssistedInjectionFactory _0) {
    return new ar.com.dcsys.gwt.assistance.client.activity.AssistanceActivityMapper(_0);
  }
  
  
  /**
   * Field for the enclosing injector.
   */
  private final ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector injector;
  public ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector_fragment(ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector injector) {
    this.injector = injector;
  }
  
}
