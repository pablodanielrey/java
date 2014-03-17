package ar.com.dcsys.gwt.assistance.client.ui.auth;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ResourcePrototype;

public class PinAuthData_PinAuthDataUiBinderImpl_GenBundle_default_InlineClientBundleGenerator implements ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData_PinAuthDataUiBinderImpl_GenBundle {
  private static PinAuthData_PinAuthDataUiBinderImpl_GenBundle_default_InlineClientBundleGenerator _instance0 = new PinAuthData_PinAuthDataUiBinderImpl_GenBundle_default_InlineClientBundleGenerator();
  private void styleInitializer() {
    style = new ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData_PinAuthDataUiBinderImpl_GenCss_style() {
      private boolean injected;
      public boolean ensureInjected() {
        if (!injected) {
          injected = true;
          com.google.gwt.dom.client.StyleInjector.inject(getText());
          return true;
        }
        return false;
      }
      public String getName() {
        return "style";
      }
      public String getText() {
        return com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().isRTL() ? ((".GBPJGMIDG{width:" + ("40em")  + ";margin:" + ("0"+ " " +"auto")  + ";}.GBPJGMIDA{width:" + ("100%")  + ";height:" + ("17em")  + ";-moz-border-radius:" + ("5px")  + ";border-radius:" + ("5px")  + ";border:" + ("1px"+ " " +"solid"+ " " +"#c1c1c1")  + ";background-color:" + ("#e8eef7")  + ";}.GBPJGMIDI{text-align:" + ("center")  + ";font-weight:" + ("bold")  + ";font-size:") + (("large")  + ";margin-bottom:" + ("1em")  + ";margin-top:" + ("1em")  + ";color:" + ("#2952a3")  + ";text-transform:" + ("capitalize")  + ";}.GBPJGMIDE{font-weight:" + ("bold")  + ";color:" + ("#47698a")  + ";width:" + ("6em")  + ";display:" + ("inline")  + ";}.GBPJGMIDD{width:" + ("15em")  + ";background-color:" + ("#fff") ) + (";text-align:" + ("center")  + ";display:" + ("inline")  + ";margin-right:" + ("1em")  + ";}.GBPJGMIDC{color:" + ("red")  + ";display:" + ("inline")  + ";margin:" + ("5px")  + ";}.GBPJGMIDF{border-color:" + ("red")  + ";border:" + ("2px")  + ";margin:" + ("5px")  + ";}.GBPJGMIDB{text-align:" + ("center")  + ";color:") + (("#47698a")  + ";}.GBPJGMIDH{text-align:" + ("center")  + ";margin-top:" + ("2em")  + ";}")) : ((".GBPJGMIDG{width:" + ("40em")  + ";margin:" + ("0"+ " " +"auto")  + ";}.GBPJGMIDA{width:" + ("100%")  + ";height:" + ("17em")  + ";-moz-border-radius:" + ("5px")  + ";border-radius:" + ("5px")  + ";border:" + ("1px"+ " " +"solid"+ " " +"#c1c1c1")  + ";background-color:" + ("#e8eef7")  + ";}.GBPJGMIDI{text-align:" + ("center")  + ";font-weight:" + ("bold")  + ";font-size:") + (("large")  + ";margin-bottom:" + ("1em")  + ";margin-top:" + ("1em")  + ";color:" + ("#2952a3")  + ";text-transform:" + ("capitalize")  + ";}.GBPJGMIDE{font-weight:" + ("bold")  + ";color:" + ("#47698a")  + ";width:" + ("6em")  + ";display:" + ("inline")  + ";}.GBPJGMIDD{width:" + ("15em")  + ";background-color:" + ("#fff") ) + (";text-align:" + ("center")  + ";display:" + ("inline")  + ";margin-left:" + ("1em")  + ";}.GBPJGMIDC{color:" + ("red")  + ";display:" + ("inline")  + ";margin:" + ("5px")  + ";}.GBPJGMIDF{border-color:" + ("red")  + ";border:" + ("2px")  + ";margin:" + ("5px")  + ";}.GBPJGMIDB{text-align:" + ("center")  + ";color:") + (("#47698a")  + ";}.GBPJGMIDH{text-align:" + ("center")  + ";margin-top:" + ("2em")  + ";}"));
      }
      public java.lang.String blockContent() {
        return "GBPJGMIDA";
      }
      public java.lang.String button() {
        return "GBPJGMIDB";
      }
      public java.lang.String error() {
        return "GBPJGMIDC";
      }
      public java.lang.String inputInline() {
        return "GBPJGMIDD";
      }
      public java.lang.String labelInline() {
        return "GBPJGMIDE";
      }
      public java.lang.String okMessage() {
        return "GBPJGMIDF";
      }
      public java.lang.String page() {
        return "GBPJGMIDG";
      }
      public java.lang.String panelButton() {
        return "GBPJGMIDH";
      }
      public java.lang.String title() {
        return "GBPJGMIDI";
      }
    }
    ;
  }
  private static class styleInitializer {
    static {
      _instance0.styleInitializer();
    }
    static ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData_PinAuthDataUiBinderImpl_GenCss_style get() {
      return style;
    }
  }
  public ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData_PinAuthDataUiBinderImpl_GenCss_style style() {
    return styleInitializer.get();
  }
  private static java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype> resourceMap;
  private static ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData_PinAuthDataUiBinderImpl_GenCss_style style;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      style(), 
    };
  }
  public ResourcePrototype getResource(String name) {
    if (GWT.isScript()) {
      return getResourceNative(name);
    } else {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<java.lang.String, com.google.gwt.resources.client.ResourcePrototype>();
        resourceMap.put("style", style());
      }
      return resourceMap.get(name);
    }
  }
  private native ResourcePrototype getResourceNative(String name) /*-{
    switch (name) {
      case 'style': return this.@ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData_PinAuthDataUiBinderImpl_GenBundle::style()();
    }
    return null;
  }-*/;
}
