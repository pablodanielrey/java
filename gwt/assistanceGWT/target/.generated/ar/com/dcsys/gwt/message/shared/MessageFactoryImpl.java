package ar.com.dcsys.gwt.message.shared;

public class MessageFactoryImpl extends com.google.web.bindery.autobean.gwt.client.impl.AbstractAutoBeanFactory implements ar.com.dcsys.gwt.message.shared.MessageFactory {
  @Override protected void initializeCreatorMap(com.google.web.bindery.autobean.gwt.client.impl.JsniCreatorMap map) {
    map.add(ar.com.dcsys.gwt.message.shared.Event.class, getConstructors_ar_com_dcsys_gwt_message_shared_Event());
    map.add(ar.com.dcsys.gwt.message.shared.Message.class, getConstructors_ar_com_dcsys_gwt_message_shared_Message());
    map.add(ar.com.dcsys.gwt.message.shared.Method.class, getConstructors_ar_com_dcsys_gwt_message_shared_Method());
  }
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_ar_com_dcsys_gwt_message_shared_Event() /*-{
    return [
      @ar.com.dcsys.gwt.message.shared.EventAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @ar.com.dcsys.gwt.message.shared.EventAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lar/com/dcsys/gwt/message/shared/Event;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_ar_com_dcsys_gwt_message_shared_Message() /*-{
    return [
      @ar.com.dcsys.gwt.message.shared.MessageAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @ar.com.dcsys.gwt.message.shared.MessageAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lar/com/dcsys/gwt/message/shared/Message;)
    ];
  }-*/;
  private native com.google.gwt.core.client.JsArray<com.google.gwt.core.client.JavaScriptObject> getConstructors_ar_com_dcsys_gwt_message_shared_Method() /*-{
    return [
      @ar.com.dcsys.gwt.message.shared.MethodAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;),
      @ar.com.dcsys.gwt.message.shared.MethodAutoBean::new(Lcom/google/web/bindery/autobean/shared/AutoBeanFactory;Lar/com/dcsys/gwt/message/shared/Method;)
    ];
  }-*/;
  @Override protected void initializeEnumMap() {
    enumToStringMap.put(ar.com.dcsys.gwt.message.shared.MessageType.ACK, "ACK");
    enumToStringMap.put(ar.com.dcsys.gwt.message.shared.MessageType.FUNCTION, "FUNCTION");
    enumToStringMap.put(ar.com.dcsys.gwt.message.shared.MessageType.ERROR, "ERROR");
    enumToStringMap.put(ar.com.dcsys.gwt.message.shared.MessageType.RETURN, "RETURN");
    enumToStringMap.put(ar.com.dcsys.gwt.message.shared.MessageType.EVENT, "EVENT");
    stringsToEnumsMap.put("FUNCTION", java.util.Collections.<java.lang.Enum<?>> singletonList(ar.com.dcsys.gwt.message.shared.MessageType.FUNCTION));
    stringsToEnumsMap.put("EVENT", java.util.Collections.<java.lang.Enum<?>> singletonList(ar.com.dcsys.gwt.message.shared.MessageType.EVENT));
    stringsToEnumsMap.put("ERROR", java.util.Collections.<java.lang.Enum<?>> singletonList(ar.com.dcsys.gwt.message.shared.MessageType.ERROR));
    stringsToEnumsMap.put("ACK", java.util.Collections.<java.lang.Enum<?>> singletonList(ar.com.dcsys.gwt.message.shared.MessageType.ACK));
    stringsToEnumsMap.put("RETURN", java.util.Collections.<java.lang.Enum<?>> singletonList(ar.com.dcsys.gwt.message.shared.MessageType.RETURN));
  }
  public com.google.web.bindery.autobean.shared.AutoBean event() {
    return new ar.com.dcsys.gwt.message.shared.EventAutoBean(MessageFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean message() {
    return new ar.com.dcsys.gwt.message.shared.MessageAutoBean(MessageFactoryImpl.this);
  }
  public com.google.web.bindery.autobean.shared.AutoBean method() {
    return new ar.com.dcsys.gwt.message.shared.MethodAutoBean(MessageFactoryImpl.this);
  }
}
