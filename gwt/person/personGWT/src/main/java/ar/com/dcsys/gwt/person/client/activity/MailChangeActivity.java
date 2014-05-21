package ar.com.dcsys.gwt.person.client.activity;

import java.util.List;

import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.MailBean;
import ar.com.dcsys.data.person.MailChangeBean;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.client.manager.MailChangesManager;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.manager.events.MailChangeModifiedEvent;
import ar.com.dcsys.gwt.person.client.manager.events.MailChangeModifiedEventHandler;
import ar.com.dcsys.gwt.person.client.ui.mailchange.MailChangeView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class MailChangeActivity extends AbstractActivity implements MailChangeView.Presenter {

	private final MailChangeView view;
	private final PersonsManager personsManager;
	private final MailChangesManager mailChangesManager;
	private EventBus eventBus;   
	private Person loggedPerson = null;
	private HandlerRegistration hr = null;
	
	private final MailChangeModifiedEventHandler handler = new MailChangeModifiedEventHandler() {
		@Override
		public void onMailChangeModifiedEvent(MailChangeModifiedEvent event) {
			if (view == null) {
				return;
			}
			view.clear();
			updateMailsFromPerson();
		}
	};
	
	
	@Inject
	public MailChangeActivity(PersonsManager personsManager, MailChangesManager mailChangesManager, MailChangeView addMailChangeView) {
	        this.personsManager = personsManager;
	        this.mailChangesManager = mailChangesManager;
	        this.view = addMailChangeView;
	}
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
			this.loggedPerson = null;
		
	        this.eventBus = eventBus;
	        this.view.setPresenter(this);
	        
	        this.view.clear();
	        
	        updateMailsFromPerson();
	        
	        panel.setWidget(view);
	        
	        hr = eventBus.addHandler(MailChangeModifiedEvent.TYPE, handler);
	}
	
	@Override
	public void onStop() {
		if (hr != null) {
			hr.removeHandler();
		}
		view.clear();
		view.setPresenter(null);
	}
	
	
	private void showMessage(String message) {
		eventBus.fireEvent(new MessageDialogEvent(true, message));
	}
	
	
	private void updateMailsFromPerson() {
		
		personsManager.getLoggedPerson(new Receiver<Person>() {
			@Override
			public void onError(String t) {
				showMessage(t);
			}
			@Override
			public void onSuccess(Person p) {
				
				loggedPerson = p;
				
				if (p == null) {
					showMessage("No se pudo obtener la persona logueada en el sistema");
					return;
				}
				
				mailChangesManager.findAllBy(p, new Receiver<List<ar.com.dcsys.data.person.MailChange>>() {
					@Override
					public void onSuccess(List<ar.com.dcsys.data.person.MailChange> t) {
						view.setMails(t);
					}
					@Override
					public void onError(String t) {
						showMessage("Error obteniendo los mails");
					}
				});
    		}
		});
	}
	    
	@Override
	public void persist() {
	             
		final String mailChangeText = view.getMail();
	    final String mailChangeTextRepeat = view.getMailRepeat();
	    
	   
	    if (mailChangeText == null && (mailChangeText.trim().equals(""))) {
	    	showMessage("Error: El email no puede estar vacio");
	        return;   
	    }
	   
	    if(!mailChangeText.equals(mailChangeTextRepeat.trim())){
	        showMessage("Error: Los emails no coinciden");
	        return;
	    }
	
	    if (loggedPerson == null) {
	    	showMessage("No se ha podido encontrar la persona logueada en el sistema");
	    	return;
	    }
	    
	    
	    Mail mail = new MailBean();
	    mail.setMail(mailChangeText);
	    ar.com.dcsys.data.person.MailChange mailChange = new MailChangeBean();
	    mailChange.setMail(mail);
	    mailChange.setConfirmed(false);
	    mailChange.setToken(null);
	
	    mailChangesManager.persist(mailChange, loggedPerson, new Receiver<Void>() {  
	    	@Override
	        public void onSuccess(Void v) {
	    		updateMailsFromPerson();
	    	}
	    	@Override
	        public void onError(String t) {
	           	showMessage(t);
	    	}
	    });  
	    
	}
	    
	@Override
	public void remove(ar.com.dcsys.data.person.MailChange change) {
		
		mailChangesManager.remove(change, new Receiver<Void>() {
			@Override
			public void onSuccess(Void t) {
				updateMailsFromPerson();
			}
			
			@Override
			public void onError(String t) {
				showMessage(t);
			}
		});
		
	}
	
	@Override
	public void sendConfirmation(ar.com.dcsys.data.person.MailChange change) {
	}
	        
}
