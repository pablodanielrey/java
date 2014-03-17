package ar.com.dcsys.gwt.message.shared;

public class EventAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<ar.com.dcsys.gwt.message.shared.Event> {
  private final ar.com.dcsys.gwt.message.shared.Event shim = new ar.com.dcsys.gwt.message.shared.Event() {
    public java.lang.String getName()  {
      java.lang.String toReturn = EventAutoBean.this.getWrapped().getName();
      return toReturn;
    }
    public java.lang.String getParams()  {
      java.lang.String toReturn = EventAutoBean.this.getWrapped().getParams();
      return toReturn;
    }
    public void setName(java.lang.String name)  {
      EventAutoBean.this.getWrapped().setName(name);
      EventAutoBean.this.set("setName", name);
    }
    public void setParams(java.lang.String params)  {
      EventAutoBean.this.getWrapped().setParams(params);
      EventAutoBean.this.set("setParams", params);
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
  public EventAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public EventAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, ar.com.dcsys.gwt.message.shared.Event wrapped) {
    super(wrapped, factory);
  }
  public ar.com.dcsys.gwt.message.shared.Event as() {return shim;}
  public Class<ar.com.dcsys.gwt.message.shared.Event> getType() {return ar.com.dcsys.gwt.message.shared.Event.class;}
  @Override protected ar.com.dcsys.gwt.message.shared.Event createSimplePeer() {
    return new ar.com.dcsys.gwt.message.shared.Event() {
      private final com.google.web.bindery.autobean.shared.Splittable data = ar.com.dcsys.gwt.message.shared.EventAutoBean.this.data;
      public java.lang.String getName()  {
        return (java.lang.String) EventAutoBean.this.getOrReify("name");
      }
      public java.lang.String getParams()  {
        return (java.lang.String) EventAutoBean.this.getOrReify("params");
      }
      public void setName(java.lang.String name)  {
        EventAutoBean.this.setProperty("name", name);
      }
      public void setParams(java.lang.String params)  {
        EventAutoBean.this.setProperty("params", params);
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    ar.com.dcsys.gwt.message.shared.Event as = as();
    value = as.getName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EventAutoBean.this, "name"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("name", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("name", value, propertyContext);
    value = as.getParams();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(EventAutoBean.this, "params"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("params", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("params", value, propertyContext);
  }
}
