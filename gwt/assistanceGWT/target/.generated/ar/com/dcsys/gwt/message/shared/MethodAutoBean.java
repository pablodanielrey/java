package ar.com.dcsys.gwt.message.shared;

public class MethodAutoBean extends com.google.web.bindery.autobean.shared.impl.AbstractAutoBean<ar.com.dcsys.gwt.message.shared.Method> {
  private final ar.com.dcsys.gwt.message.shared.Method shim = new ar.com.dcsys.gwt.message.shared.Method() {
    public java.lang.String getName()  {
      java.lang.String toReturn = MethodAutoBean.this.getWrapped().getName();
      return toReturn;
    }
    public java.lang.String getParams()  {
      java.lang.String toReturn = MethodAutoBean.this.getWrapped().getParams();
      return toReturn;
    }
    public void setName(java.lang.String name)  {
      MethodAutoBean.this.getWrapped().setName(name);
      MethodAutoBean.this.set("setName", name);
    }
    public void setParams(java.lang.String params)  {
      MethodAutoBean.this.getWrapped().setParams(params);
      MethodAutoBean.this.set("setParams", params);
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
  public MethodAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory) {super(factory);}
  public MethodAutoBean(com.google.web.bindery.autobean.shared.AutoBeanFactory factory, ar.com.dcsys.gwt.message.shared.Method wrapped) {
    super(wrapped, factory);
  }
  public ar.com.dcsys.gwt.message.shared.Method as() {return shim;}
  public Class<ar.com.dcsys.gwt.message.shared.Method> getType() {return ar.com.dcsys.gwt.message.shared.Method.class;}
  @Override protected ar.com.dcsys.gwt.message.shared.Method createSimplePeer() {
    return new ar.com.dcsys.gwt.message.shared.Method() {
      private final com.google.web.bindery.autobean.shared.Splittable data = ar.com.dcsys.gwt.message.shared.MethodAutoBean.this.data;
      public java.lang.String getName()  {
        return (java.lang.String) MethodAutoBean.this.getOrReify("name");
      }
      public java.lang.String getParams()  {
        return (java.lang.String) MethodAutoBean.this.getOrReify("params");
      }
      public void setName(java.lang.String name)  {
        MethodAutoBean.this.setProperty("name", name);
      }
      public void setParams(java.lang.String params)  {
        MethodAutoBean.this.setProperty("params", params);
      }
    };
  }
  @Override protected void traverseProperties(com.google.web.bindery.autobean.shared.AutoBeanVisitor visitor, com.google.web.bindery.autobean.shared.impl.AbstractAutoBean.OneShotContext ctx) {
    com.google.web.bindery.autobean.shared.impl.AbstractAutoBean bean;
    Object value;
    com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext propertyContext;
    ar.com.dcsys.gwt.message.shared.Method as = as();
    value = as.getName();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(MethodAutoBean.this, "name"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("name", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("name", value, propertyContext);
    value = as.getParams();
    propertyContext = new com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext(
      as,
      com.google.web.bindery.autobean.gwt.client.impl.ClientPropertyContext.Setter.beanSetter(MethodAutoBean.this, "params"),
      java.lang.String.class
    );
    if (visitor.visitValueProperty("params", value, propertyContext)) {
    }
    visitor.endVisitValueProperty("params", value, propertyContext);
  }
}
