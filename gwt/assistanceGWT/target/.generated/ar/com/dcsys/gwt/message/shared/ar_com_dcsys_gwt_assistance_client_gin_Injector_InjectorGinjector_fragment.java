package ar.com.dcsys.gwt.message.shared;

import com.google.gwt.core.client.GWT;
import ar.com.dcsys.gwt.assistance.client.gin.ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector;

public class ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector_fragment {
  public void memberInject_Key$type$ar$com$dcsys$gwt$message$shared$MessageFactory$_annotation$$none$$(ar.com.dcsys.gwt.message.shared.MessageFactory injectee) {
    
  }
  
  public void memberInject_Key$type$ar$com$dcsys$gwt$message$shared$MessageEncoderDecoder$_annotation$$none$$(ar.com.dcsys.gwt.message.shared.MessageEncoderDecoder injectee) {
    
  }
  
  
  /**
   * Binding for ar.com.dcsys.gwt.message.shared.MessageFactory declared at:
   *   Implicit GWT.create binding for ar.com.dcsys.gwt.message.shared.MessageFactory
   */
  public ar.com.dcsys.gwt.message.shared.MessageFactory get_Key$type$ar$com$dcsys$gwt$message$shared$MessageFactory$_annotation$$none$$() {
    Object created = GWT.create(ar.com.dcsys.gwt.message.shared.MessageFactory.class);
    assert created instanceof ar.com.dcsys.gwt.message.shared.MessageFactory;
    ar.com.dcsys.gwt.message.shared.MessageFactory result = (ar.com.dcsys.gwt.message.shared.MessageFactory) created;
    
    memberInject_Key$type$ar$com$dcsys$gwt$message$shared$MessageFactory$_annotation$$none$$(result);
    
    return result;
    
  }
  
  private ar.com.dcsys.gwt.message.shared.MessageEncoderDecoder singleton_Key$type$ar$com$dcsys$gwt$message$shared$MessageEncoderDecoder$_annotation$$none$$ = null;
  
  public ar.com.dcsys.gwt.message.shared.MessageEncoderDecoder get_Key$type$ar$com$dcsys$gwt$message$shared$MessageEncoderDecoder$_annotation$$none$$() {
    
    if (singleton_Key$type$ar$com$dcsys$gwt$message$shared$MessageEncoderDecoder$_annotation$$none$$ == null) {
    ar.com.dcsys.gwt.message.shared.MessageEncoderDecoder result = ar$com$dcsys$gwt$message$shared$MessageEncoderDecoder_ar$com$dcsys$gwt$message$shared$MessageEncoderDecoder_methodInjection(get_Key$type$ar$com$dcsys$gwt$message$shared$MessageFactory$_annotation$$none$$());
    memberInject_Key$type$ar$com$dcsys$gwt$message$shared$MessageEncoderDecoder$_annotation$$none$$(result);
    
        singleton_Key$type$ar$com$dcsys$gwt$message$shared$MessageEncoderDecoder$_annotation$$none$$ = result;
    }
    return singleton_Key$type$ar$com$dcsys$gwt$message$shared$MessageEncoderDecoder$_annotation$$none$$;
    
  }
  
  public ar.com.dcsys.gwt.message.shared.MessageEncoderDecoder ar$com$dcsys$gwt$message$shared$MessageEncoderDecoder_ar$com$dcsys$gwt$message$shared$MessageEncoderDecoder_methodInjection(ar.com.dcsys.gwt.message.shared.MessageFactory _0) {
    return new ar.com.dcsys.gwt.message.shared.MessageEncoderDecoder(_0);
  }
  
  
  /**
   * Field for the enclosing injector.
   */
  private final ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector injector;
  public ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector_fragment(ar_com_dcsys_gwt_assistance_client_gin_Injector_InjectorGinjector injector) {
    this.injector = injector;
  }
  
}
