package ar.com.dcsys.gwt.person.client.ui.mailchange;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

public interface MailChangeView extends IsWidget {

	public void clear(); 
	public void setPresenter(Presenter presenter);
	
	public void setMail(String mail);
	public String getMail();
	
	public void setMailRepeat(String mailRepeat);
	public String getMailRepeat();
	
	public void setMails(List<ar.com.dcsys.data.person.MailChange> mails);
	
    public interface Presenter {
        public void persist();
        public void remove(ar.com.dcsys.data.person.MailChange change);
        public void sendConfirmation(ar.com.dcsys.data.person.MailChange change);
    }
}
