package ar.com.dcsys.gwt.person.client.gin;

import ar.com.dcsys.gwt.person.client.manager.MailChangesManager;
import ar.com.dcsys.gwt.person.client.manager.MailChangesManagerBean;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.manager.PersonsManagerBean;
import ar.com.dcsys.gwt.person.shared.DocumentEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.DocumentFactory;
import ar.com.dcsys.gwt.person.shared.PersonEncoderDecoder;
import ar.com.dcsys.gwt.person.shared.PersonFactory;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class PersonModelGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		
		bind(PersonFactory.class);
		bind(PersonEncoderDecoder.class).in(Singleton.class);
		bind(DocumentFactory.class);
		bind(DocumentEncoderDecoder.class).in(Singleton.class);
		bind(PersonsManager.class).to(PersonsManagerBean.class).in(Singleton.class);
		bind(MailChangesManager.class).to(MailChangesManagerBean.class).in(Singleton.class);
		
	}

}
