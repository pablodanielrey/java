package ar.com.dcsys.gwt.assistance.client.ui.auth;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.uibinder.client.UiBinderUtil;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiBinderUtil;
import com.google.gwt.user.client.ui.Widget;

public class PinAuthData_PinAuthDataUiBinderImpl implements UiBinder<com.google.gwt.user.client.ui.Widget, ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData>, ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData.PinAuthDataUiBinder {

  interface Template extends SafeHtmlTemplates {
    @Template("Mensaje")
    SafeHtml html1();
     
  }

  Template template = GWT.create(Template.class);


  public com.google.gwt.user.client.ui.Widget createAndBindUi(final ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData owner) {


    return new Widgets(owner).get_f_FlowPanel1();
  }

  /**
   * Encapsulates the access to all inner widgets
   */
  class Widgets {
    private final ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData owner;


    final com.google.gwt.event.dom.client.ClickHandler handlerMethodWithNameVeryUnlikelyToCollideWithUserFieldNames1 = (com.google.gwt.event.dom.client.ClickHandler) new com.google.gwt.event.dom.client.ClickHandler() {
      public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
        owner.onOkMessage((com.google.gwt.event.dom.client.ClickEvent) event);
      }
    };

    final com.google.gwt.event.dom.client.ClickHandler handlerMethodWithNameVeryUnlikelyToCollideWithUserFieldNames2 = (com.google.gwt.event.dom.client.ClickHandler) new com.google.gwt.event.dom.client.ClickHandler() {
      public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
        owner.onSave((com.google.gwt.event.dom.client.ClickEvent) event);
      }
    };

    public Widgets(final ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData owner) {
      this.owner = owner;
      build_style();  // generated css resource must be always created. Type: GENERATED_CSS. Precedence: 1
    }

    SafeHtml template_html1() {
      return template.html1();
    }

    /**
     * Getter for clientBundleFieldNameUnlikelyToCollideWithUserSpecifiedFieldOkay called 1 times. Type: GENERATED_BUNDLE. Build precedence: 1.
     */
    private ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData_PinAuthDataUiBinderImpl_GenBundle get_clientBundleFieldNameUnlikelyToCollideWithUserSpecifiedFieldOkay() {
      return build_clientBundleFieldNameUnlikelyToCollideWithUserSpecifiedFieldOkay();
    }
    private ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData_PinAuthDataUiBinderImpl_GenBundle build_clientBundleFieldNameUnlikelyToCollideWithUserSpecifiedFieldOkay() {
      // Creation section.
      final ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData_PinAuthDataUiBinderImpl_GenBundle clientBundleFieldNameUnlikelyToCollideWithUserSpecifiedFieldOkay = (ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData_PinAuthDataUiBinderImpl_GenBundle) GWT.create(ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData_PinAuthDataUiBinderImpl_GenBundle.class);
      // Setup section.


      return clientBundleFieldNameUnlikelyToCollideWithUserSpecifiedFieldOkay;
    }

    /**
     * Getter for style called 14 times. Type: GENERATED_CSS. Build precedence: 1.
     */
    private ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData_PinAuthDataUiBinderImpl_GenCss_style style;
    private ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData_PinAuthDataUiBinderImpl_GenCss_style get_style() {
      return style;
    }
    private ar.com.dcsys.gwt.assistance.client.ui.auth.PinAuthData_PinAuthDataUiBinderImpl_GenCss_style build_style() {
      // Creation section.
      style = get_clientBundleFieldNameUnlikelyToCollideWithUserSpecifiedFieldOkay().style();
      // Setup section.
      style.ensureInjected();


      return style;
    }

    /**
     * Getter for f_FlowPanel1 called 1 times. Type: DEFAULT. Build precedence: 1.
     */
    private com.google.gwt.user.client.ui.FlowPanel get_f_FlowPanel1() {
      return build_f_FlowPanel1();
    }
    private com.google.gwt.user.client.ui.FlowPanel build_f_FlowPanel1() {
      // Creation section.
      final com.google.gwt.user.client.ui.FlowPanel f_FlowPanel1 = (com.google.gwt.user.client.ui.FlowPanel) GWT.create(com.google.gwt.user.client.ui.FlowPanel.class);
      // Setup section.
      f_FlowPanel1.add(get_f_Label2());
      f_FlowPanel1.add(get_f_FlowPanel3());
      f_FlowPanel1.add(get_f_SimplePanel9());
      f_FlowPanel1.setStyleName("" + get_style().page() + "");


      return f_FlowPanel1;
    }

    /**
     * Getter for f_Label2 called 1 times. Type: DEFAULT. Build precedence: 2.
     */
    private com.google.gwt.user.client.ui.Label get_f_Label2() {
      return build_f_Label2();
    }
    private com.google.gwt.user.client.ui.Label build_f_Label2() {
      // Creation section.
      final com.google.gwt.user.client.ui.Label f_Label2 = (com.google.gwt.user.client.ui.Label) GWT.create(com.google.gwt.user.client.ui.Label.class);
      // Setup section.
      f_Label2.setStyleName("" + get_style().title() + "");
      f_Label2.setText("Modificación del PIN");


      return f_Label2;
    }

    /**
     * Getter for f_FlowPanel3 called 1 times. Type: DEFAULT. Build precedence: 2.
     */
    private com.google.gwt.user.client.ui.FlowPanel get_f_FlowPanel3() {
      return build_f_FlowPanel3();
    }
    private com.google.gwt.user.client.ui.FlowPanel build_f_FlowPanel3() {
      // Creation section.
      final com.google.gwt.user.client.ui.FlowPanel f_FlowPanel3 = (com.google.gwt.user.client.ui.FlowPanel) GWT.create(com.google.gwt.user.client.ui.FlowPanel.class);
      // Setup section.
      f_FlowPanel3.add(get_f_FlowPanel4());
      f_FlowPanel3.add(get_f_FlowPanel6());
      f_FlowPanel3.add(get_f_FlowPanel8());
      f_FlowPanel3.setStyleName("" + get_style().blockContent() + "");


      return f_FlowPanel3;
    }

    /**
     * Getter for f_FlowPanel4 called 1 times. Type: DEFAULT. Build precedence: 3.
     */
    private com.google.gwt.user.client.ui.FlowPanel get_f_FlowPanel4() {
      return build_f_FlowPanel4();
    }
    private com.google.gwt.user.client.ui.FlowPanel build_f_FlowPanel4() {
      // Creation section.
      final com.google.gwt.user.client.ui.FlowPanel f_FlowPanel4 = (com.google.gwt.user.client.ui.FlowPanel) GWT.create(com.google.gwt.user.client.ui.FlowPanel.class);
      // Setup section.
      f_FlowPanel4.add(get_f_Label5());
      f_FlowPanel4.add(get_pin());
      f_FlowPanel4.add(get_pinError());


      return f_FlowPanel4;
    }

    /**
     * Getter for f_Label5 called 1 times. Type: DEFAULT. Build precedence: 4.
     */
    private com.google.gwt.user.client.ui.Label get_f_Label5() {
      return build_f_Label5();
    }
    private com.google.gwt.user.client.ui.Label build_f_Label5() {
      // Creation section.
      final com.google.gwt.user.client.ui.Label f_Label5 = (com.google.gwt.user.client.ui.Label) GWT.create(com.google.gwt.user.client.ui.Label.class);
      // Setup section.
      f_Label5.setStyleName("" + get_style().labelInline() + "");
      f_Label5.setText("PIN:");


      return f_Label5;
    }

    /**
     * Getter for pin called 1 times. Type: DEFAULT. Build precedence: 4.
     */
    private com.google.gwt.user.client.ui.PasswordTextBox get_pin() {
      return build_pin();
    }
    private com.google.gwt.user.client.ui.PasswordTextBox build_pin() {
      // Creation section.
      final com.google.gwt.user.client.ui.PasswordTextBox pin = (com.google.gwt.user.client.ui.PasswordTextBox) GWT.create(com.google.gwt.user.client.ui.PasswordTextBox.class);
      // Setup section.
      pin.setStyleName("" + get_style().inputInline() + "");


      this.owner.pin = pin;

      return pin;
    }

    /**
     * Getter for pinError called 1 times. Type: DEFAULT. Build precedence: 4.
     */
    private com.google.gwt.user.client.ui.Label get_pinError() {
      return build_pinError();
    }
    private com.google.gwt.user.client.ui.Label build_pinError() {
      // Creation section.
      final com.google.gwt.user.client.ui.Label pinError = (com.google.gwt.user.client.ui.Label) GWT.create(com.google.gwt.user.client.ui.Label.class);
      // Setup section.
      pinError.setStyleName("" + get_style().error() + "");
      pinError.setText("PIN No Permitido");
      pinError.setVisible(false);


      this.owner.pinError = pinError;

      return pinError;
    }

    /**
     * Getter for f_FlowPanel6 called 1 times. Type: DEFAULT. Build precedence: 3.
     */
    private com.google.gwt.user.client.ui.FlowPanel get_f_FlowPanel6() {
      return build_f_FlowPanel6();
    }
    private com.google.gwt.user.client.ui.FlowPanel build_f_FlowPanel6() {
      // Creation section.
      final com.google.gwt.user.client.ui.FlowPanel f_FlowPanel6 = (com.google.gwt.user.client.ui.FlowPanel) GWT.create(com.google.gwt.user.client.ui.FlowPanel.class);
      // Setup section.
      f_FlowPanel6.add(get_f_Label7());
      f_FlowPanel6.add(get_pin2());


      return f_FlowPanel6;
    }

    /**
     * Getter for f_Label7 called 1 times. Type: DEFAULT. Build precedence: 4.
     */
    private com.google.gwt.user.client.ui.Label get_f_Label7() {
      return build_f_Label7();
    }
    private com.google.gwt.user.client.ui.Label build_f_Label7() {
      // Creation section.
      final com.google.gwt.user.client.ui.Label f_Label7 = (com.google.gwt.user.client.ui.Label) GWT.create(com.google.gwt.user.client.ui.Label.class);
      // Setup section.
      f_Label7.setStyleName("" + get_style().labelInline() + "");
      f_Label7.setText("Repetir PIN:");


      return f_Label7;
    }

    /**
     * Getter for pin2 called 1 times. Type: DEFAULT. Build precedence: 4.
     */
    private com.google.gwt.user.client.ui.PasswordTextBox get_pin2() {
      return build_pin2();
    }
    private com.google.gwt.user.client.ui.PasswordTextBox build_pin2() {
      // Creation section.
      final com.google.gwt.user.client.ui.PasswordTextBox pin2 = (com.google.gwt.user.client.ui.PasswordTextBox) GWT.create(com.google.gwt.user.client.ui.PasswordTextBox.class);
      // Setup section.
      pin2.setStyleName("" + get_style().inputInline() + "");


      this.owner.pin2 = pin2;

      return pin2;
    }

    /**
     * Getter for f_FlowPanel8 called 1 times. Type: DEFAULT. Build precedence: 3.
     */
    private com.google.gwt.user.client.ui.FlowPanel get_f_FlowPanel8() {
      return build_f_FlowPanel8();
    }
    private com.google.gwt.user.client.ui.FlowPanel build_f_FlowPanel8() {
      // Creation section.
      final com.google.gwt.user.client.ui.FlowPanel f_FlowPanel8 = (com.google.gwt.user.client.ui.FlowPanel) GWT.create(com.google.gwt.user.client.ui.FlowPanel.class);
      // Setup section.
      f_FlowPanel8.add(get_save());
      f_FlowPanel8.add(get_pin2Error());
      f_FlowPanel8.setStyleName("" + get_style().panelButton() + "");


      return f_FlowPanel8;
    }

    /**
     * Getter for save called 1 times. Type: DEFAULT. Build precedence: 4.
     */
    private com.google.gwt.user.client.ui.Button get_save() {
      return build_save();
    }
    private com.google.gwt.user.client.ui.Button build_save() {
      // Creation section.
      final com.google.gwt.user.client.ui.Button save = (com.google.gwt.user.client.ui.Button) GWT.create(com.google.gwt.user.client.ui.Button.class);
      // Setup section.
      save.setStyleName("" + get_style().button() + "");
      save.setEnabled(false);
      save.setText("Guardar");
      save.setVisible(true);
      save.addClickHandler(handlerMethodWithNameVeryUnlikelyToCollideWithUserFieldNames2);


      this.owner.save = save;

      return save;
    }

    /**
     * Getter for pin2Error called 1 times. Type: DEFAULT. Build precedence: 4.
     */
    private com.google.gwt.user.client.ui.Label get_pin2Error() {
      return build_pin2Error();
    }
    private com.google.gwt.user.client.ui.Label build_pin2Error() {
      // Creation section.
      final com.google.gwt.user.client.ui.Label pin2Error = (com.google.gwt.user.client.ui.Label) GWT.create(com.google.gwt.user.client.ui.Label.class);
      // Setup section.
      pin2Error.setStyleName("" + get_style().error() + "");
      pin2Error.setText("PIN diferentes!!");
      pin2Error.setVisible(false);


      this.owner.pin2Error = pin2Error;

      return pin2Error;
    }

    /**
     * Getter for f_SimplePanel9 called 1 times. Type: DEFAULT. Build precedence: 2.
     */
    private com.google.gwt.user.client.ui.SimplePanel get_f_SimplePanel9() {
      return build_f_SimplePanel9();
    }
    private com.google.gwt.user.client.ui.SimplePanel build_f_SimplePanel9() {
      // Creation section.
      final com.google.gwt.user.client.ui.SimplePanel f_SimplePanel9 = (com.google.gwt.user.client.ui.SimplePanel) GWT.create(com.google.gwt.user.client.ui.SimplePanel.class);
      // Setup section.
      f_SimplePanel9.add(get_messageDialog());


      return f_SimplePanel9;
    }

    /**
     * Getter for messageDialog called 1 times. Type: DEFAULT. Build precedence: 3.
     */
    private com.google.gwt.user.client.ui.DialogBox get_messageDialog() {
      return build_messageDialog();
    }
    private com.google.gwt.user.client.ui.DialogBox build_messageDialog() {
      // Creation section.
      final com.google.gwt.user.client.ui.DialogBox messageDialog = new com.google.gwt.user.client.ui.DialogBox(false, true);
      // Setup section.
      messageDialog.setHTML(template_html1().asString());
      messageDialog.setWidget(get_f_FlowPanel10());
      messageDialog.setVisible(false);


      this.owner.messageDialog = messageDialog;

      return messageDialog;
    }

    /**
     * Getter for f_FlowPanel10 called 1 times. Type: DEFAULT. Build precedence: 4.
     */
    private com.google.gwt.user.client.ui.FlowPanel get_f_FlowPanel10() {
      return build_f_FlowPanel10();
    }
    private com.google.gwt.user.client.ui.FlowPanel build_f_FlowPanel10() {
      // Creation section.
      final com.google.gwt.user.client.ui.FlowPanel f_FlowPanel10 = (com.google.gwt.user.client.ui.FlowPanel) GWT.create(com.google.gwt.user.client.ui.FlowPanel.class);
      // Setup section.
      f_FlowPanel10.add(get_f_FlowPanel11());
      f_FlowPanel10.add(get_f_FlowPanel12());


      return f_FlowPanel10;
    }

    /**
     * Getter for f_FlowPanel11 called 1 times. Type: DEFAULT. Build precedence: 5.
     */
    private com.google.gwt.user.client.ui.FlowPanel get_f_FlowPanel11() {
      return build_f_FlowPanel11();
    }
    private com.google.gwt.user.client.ui.FlowPanel build_f_FlowPanel11() {
      // Creation section.
      final com.google.gwt.user.client.ui.FlowPanel f_FlowPanel11 = (com.google.gwt.user.client.ui.FlowPanel) GWT.create(com.google.gwt.user.client.ui.FlowPanel.class);
      // Setup section.
      f_FlowPanel11.add(get_message());
      f_FlowPanel11.setStyleName("" + get_style().okMessage() + "");


      return f_FlowPanel11;
    }

    /**
     * Getter for message called 1 times. Type: DEFAULT. Build precedence: 6.
     */
    private com.google.gwt.user.client.ui.Label get_message() {
      return build_message();
    }
    private com.google.gwt.user.client.ui.Label build_message() {
      // Creation section.
      final com.google.gwt.user.client.ui.Label message = (com.google.gwt.user.client.ui.Label) GWT.create(com.google.gwt.user.client.ui.Label.class);
      // Setup section.


      this.owner.message = message;

      return message;
    }

    /**
     * Getter for f_FlowPanel12 called 1 times. Type: DEFAULT. Build precedence: 5.
     */
    private com.google.gwt.user.client.ui.FlowPanel get_f_FlowPanel12() {
      return build_f_FlowPanel12();
    }
    private com.google.gwt.user.client.ui.FlowPanel build_f_FlowPanel12() {
      // Creation section.
      final com.google.gwt.user.client.ui.FlowPanel f_FlowPanel12 = (com.google.gwt.user.client.ui.FlowPanel) GWT.create(com.google.gwt.user.client.ui.FlowPanel.class);
      // Setup section.
      f_FlowPanel12.add(get_okMessage());
      f_FlowPanel12.setStyleName("" + get_style().panelButton() + "");


      return f_FlowPanel12;
    }

    /**
     * Getter for okMessage called 1 times. Type: DEFAULT. Build precedence: 6.
     */
    private com.google.gwt.user.client.ui.Button get_okMessage() {
      return build_okMessage();
    }
    private com.google.gwt.user.client.ui.Button build_okMessage() {
      // Creation section.
      final com.google.gwt.user.client.ui.Button okMessage = (com.google.gwt.user.client.ui.Button) GWT.create(com.google.gwt.user.client.ui.Button.class);
      // Setup section.
      okMessage.setStyleName("" + get_style().button() + "");
      okMessage.setText("Cerrar");
      okMessage.addClickHandler(handlerMethodWithNameVeryUnlikelyToCollideWithUserFieldNames1);


      this.owner.okMessage = okMessage;

      return okMessage;
    }
  }
}
