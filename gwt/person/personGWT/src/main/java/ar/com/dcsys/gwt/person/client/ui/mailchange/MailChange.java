package ar.com.dcsys.gwt.person.client.ui.mailchange;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.ActionCell.Delegate;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.IdentityColumn;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MailChange extends Composite implements MailChangeView {

    private static EmailAdminUiBinder uiBinder = GWT.create(EmailAdminUiBinder.class);

    interface EmailAdminUiBinder extends UiBinder<Widget, MailChange> {
    }

    public MailChange() {
            createMailInfos();
            initWidget(uiBinder.createAndBindUi(this));
    }

    private Presenter presenter ;
    @UiField TextBox mail;
    @UiField TextBox mailRepeat;
    @UiField(provided = true) DataGrid<ar.com.dcsys.data.person.MailChange> mailDatas;
    
    
    private void createMailInfos(){

            mailDatas = new DataGrid<ar.com.dcsys.data.person.MailChange>();

            //columna: email
            TextColumn<ar.com.dcsys.data.person.MailChange> mail = new TextColumn<ar.com.dcsys.data.person.MailChange>() {
                  @Override
                  public String getValue(ar.com.dcsys.data.person.MailChange mailData) {
                          if (mailData == null) {
                                  return "";
                          }
                          
                          String mail = mailData.getMail().getMail();
                          return mail;
                  }
            };
            mailDatas.addColumn(mail, "Email");
            
            //columna: emailStatus
            TextColumn<ar.com.dcsys.data.person.MailChange> mailStatus = new TextColumn<ar.com.dcsys.data.person.MailChange>() {
                  @Override
                  public String getValue(ar.com.dcsys.data.person.MailChange mailData) {
                          if (mailData == null) {
                                  return "";
                          }

                          if (mailData.isConfirmed()) {
                        	  return "confirmado";
                          } else {
                        	  return "pendiente de confirmación";
                          }
                  }
            };
            mailDatas.addColumn(mailStatus, "Estado");
            

            //columna: boton reenviar confirmacion
            ActionCell<ar.com.dcsys.data.person.MailChange> sendConfirmationCell = new ActionCell<ar.com.dcsys.data.person.MailChange>("Reenviar confirmacion",new Delegate<ar.com.dcsys.data.person.MailChange>() {
                    @Override
                    public void execute(ar.com.dcsys.data.person.MailChange mailData) {
                            if (mailData == null) {
                                  return;
                            }
                            
                            if (presenter == null) {
                                    return;
                            }

                            if (!mailData.isConfirmed()) {
                                presenter.sendConfirmation(mailData);
                            }
                    }
            }) {
            	public void render(com.google.gwt.cell.client.Cell.Context context, ar.com.dcsys.data.person.MailChange value, com.google.gwt.safehtml.shared.SafeHtmlBuilder sb) {
            		if (value == null || !value.isConfirmed()) {
            			super.render(context, value, sb);
            		} else {
            			sb.appendEscaped("");
            		}
            	};
            };
            IdentityColumn<ar.com.dcsys.data.person.MailChange> sendConfirmation = new IdentityColumn<ar.com.dcsys.data.person.MailChange>(sendConfirmationCell);
            mailDatas.addColumn(sendConfirmation);
                            
                            
            //columna: boton eliminar
            ActionCell<ar.com.dcsys.data.person.MailChange> deleteCell = new ActionCell<ar.com.dcsys.data.person.MailChange>("Eliminar",new Delegate<ar.com.dcsys.data.person.MailChange>() {
                    @Override
                    public void execute(ar.com.dcsys.data.person.MailChange mailData) {
                            if (mailData == null) {
                                  return;
                        }
                            
                        if (presenter == null) {
                                return;
                        }
                        presenter.remove(mailData);
                    }
            });
            IdentityColumn<ar.com.dcsys.data.person.MailChange> delete = new IdentityColumn<ar.com.dcsys.data.person.MailChange>(deleteCell);
            mailDatas.addColumn(delete);
    }

    @Override
    public void setPresenter(Presenter presenter) {
            this.presenter = presenter;
    }

    @Override
    public void clear() {
            mail.setText("");
            mailRepeat.setText("");
            mailDatas.setRowCount(0,true); 
            mailDatas.setRowData(new ArrayList<ar.com.dcsys.data.person.MailChange>());
    }

    @Override
    public String getMail() {
            return mail.getText();
    }
    
    @Override
    public String getMailRepeat() {
            return mailRepeat.getText();
    }

            
    @UiHandler("save")
    public void onClickSave(ClickEvent e) {
    	if (presenter == null) {
    		return;
    	}
    	presenter.persist();
    }

    @Override
    public void setMails(List<ar.com.dcsys.data.person.MailChange> mailDatas) {
            this.mailDatas.setRowData(mailDatas);
    }

    @Override
    public void setMail(String mail) {
            this.mail.setText(mail);
    }

    @Override
    public void setMailRepeat(String mailRepeat) {
            this.mailRepeat.setText(mailRepeat);        
    }


}
