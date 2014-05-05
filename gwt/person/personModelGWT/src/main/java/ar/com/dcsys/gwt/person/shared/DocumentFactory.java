package ar.com.dcsys.gwt.person.shared;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.gwt.person.shared.lists.DocumentList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface DocumentFactory extends AutoBeanFactory {

	public AutoBean<Document> document();
	public AutoBean<Document> document(Document d);
	public AutoBean<DocumentList> documentList();
	
	
}
