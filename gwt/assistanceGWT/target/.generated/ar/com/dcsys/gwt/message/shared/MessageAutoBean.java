package ar.com.dcsys.gwt.message.shared;

public class MessageAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<ar.com.dcsys.gwt.message.shared.Message> {
  private final ar.com.dcsys.gwt.message.shared.Message shim = new ar.com.dcsys.gwt.message.shared.Message() {
    public ar.com.dcsys.gwt.message.shared.MessageType getType()  {
      ar.com.dcsys.gwt.message.shared.MessageType toReturn = MessageAutoBean.this.getWrapped().getType();
      return toReturn;
    }
    public java.lang.String getId()  {
      java.lang.String toReturn = MessageAutoBean.this.getWrapped().getId();
      return toReturn;
    }
    public java.lang.String getPayload()  {
      java.lang.String toReturn = MessageAutoBean.this.getWrapped().getPayload();
      return toReturn;
    }
    public java.lang.String getSessionId()  {
      java.lang.String toReturn = MessageAutoBean.this.getWrapped().getSessionId();
      return toReturn;
    }
    public void setId(java.lang.String id)  {
      MessageAutoBean.this.getWrapped().setId(id);
      MessageAutoBean.this.set("setId", id);
    }
    public void setPayload(java.lang.String p)  {
      MessageAutoBean.this.getWrapped().setPayload(p);
      MessageAutoBean.this.set("setPayload", p);
    }
    public void setSessionId(java.lang.String id)  {
      MessageAutoBean.this.getWrapped().setSessionId(id);
      MessageAutoBean.this.set("setSessionId", id);
    }
    public void setType(ar.com.dcsys.gwt.message.shared.MessageType type)  {
      MessageAutoBean.this.getWrapped().setType(type);
      MessageAutoBean.this.set("setType", type);
    }
    @Override public boolean equals(Object o) {
      return this == o || getWrapped().equals(o);
    }
    @Override public int hashCode() {
      return getWrapped().hashCode();
    }
    @Override public String toString() {
      return getWrapped().toString();
    }
  };
  { com.google.gwt.core.client.impl.WeakMapping.set(shim, com.google.web.bindery.autobean.shared.AutoBean.class.getName(), this); }
  public MessageAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public MessageAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, ar.com.dcsys.gwt.message.shared.Message wrapped) {
    super(wrapped, factory);
  }
  public ar.com.dcsys.gwt.message.shared.Message as() {return shim;}
  public Class<ar.com.dcsys.gwt.message.shared.Message> getType() {return ar.com.dcsys.gwt.message.shared.Message.class;}
  @Override protected ar.com.dcsys.gwt.message.shared.Message createSimplePeer() {
    return new ar.com.dcsys.gwt.message.shared.Message() {
      private final com.google.web.bindery.autobean.shared.Splittable data = ar.com.dcsys.gwt.message.shared.MessageAutoBean.this.data;
      public ar.com.dcsys.gwt.message.shared.MessageType getType()  {
        return (ar.com.dcsys.gwt.message.shared.MessageType) MessageAutoBean.this.getOrReify("type");
      }
      public java.lang.String getId()  {
        return (java.lang.String) MessageAutoBean.this.getOrReify("id");
      }
      public java.lang.String getPayload()  {
        return (java.lang.String) MessageAutoBean.this.getOrReify("payload");
      }
      public java.lang.String getSessionId()  {
        return (java.lang.String) MessageAutoBean.this.getOrReify("sessionId");
      }
      public void setId(java.lang.String id)  {
        MessageAutoBean.this.setProperty("id", id);
      }
      public void setPayload(java.lang.String p)  {
        MessageAutoBean.this.setProperty("payload", p);
      }
      public void setSessionId(java.lang.String id)  {
        MessageAutoBean.this.setProperty("sessionId", id);
      }
      public void setType(ar.com.dcsys.gwt.message.shared.MessageType type)  {
        MessageAutoBean.this.setProperty("type", type);
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    ar.com.dcsys.gwt.message.shared.Message as = as();
    value = as.getType();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(MessageAutoBean.this, "type"),
      ar.com.dcsys.gwt.message.shared.MessageType.class
    );
    if (visitor.visitValueProperty("type", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("type", value, propertyContext);
    value = as.getId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(MessageAutoBean.this, "id"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("id", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("id", value, propertyContext);
    value = as.getPayload();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(MessageAutoBean.this, "payload"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("payload", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("payload", value, propertyContext);
    value = as.getSessionId();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(MessageAutoBean.this, "sessionId"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("sessionId", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("sessionId", value, propertyContext);
  }
}
