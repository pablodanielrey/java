package ar.com.dcsys.gwt.person.shared;

import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.gwt.autoBeans.shared.AutoBeanUtils;
import ar.com.dcsys.gwt.person.shared.lists.DocumentList;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class DocumentEncoderDecoder {

	private final DocumentFactory documentFactory;
	
	@Inject
	public DocumentEncoderDecoder(DocumentFactory documentFactory) {
		this.documentFactory = documentFactory;
	}
	
	public String encode(Document d) {
		AutoBean<Document> ad = documentFactory.create(Document.class,d);
		String json = AutoBeanCodex.encode(ad).getPayload();
		return json;
	}
	
	public Document decode(String d) {
		AutoBean<Document> ad = AutoBeanCodex.decode(documentFactory, Document.class, d);
		return ad.as();
	}
	
	
	public String encodeDocumentList(List<Document> ds) {
		AutoBean<DocumentList> ad = documentFactory.documentList();
		
		List<Document> dss = AutoBeanUtils.wrapList(documentFactory,Document.class,ds);
		ad.as().setList(dss);
		
		String json = AutoBeanCodex.encode(ad).getPayload();
		return json;
	}
	
	public List<Document> decodeDocumentList(String ds) {
		AutoBean<DocumentList> dss = AutoBeanCodex.decode(documentFactory, DocumentList.class, ds);
		List<Document> d = dss.as().getList();
		return d;
	}
	
}
