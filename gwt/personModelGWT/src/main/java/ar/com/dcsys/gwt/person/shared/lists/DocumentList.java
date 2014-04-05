package ar.com.dcsys.gwt.person.shared.lists;

import java.util.List;

import ar.com.dcsys.data.document.Document;

public interface DocumentList {

	public void setList(List<Document> ds);
	public List<Document> getList();
	
}
