package ar.com.dcsys.model;

import java.util.List;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.exceptions.DocumentException;

public interface DocumentsManager {

	public void persist(Document d) throws DocumentException;
	public List<String> findAll() throws DocumentException;
	public Document findById(String id) throws DocumentException;
	
}
