package ar.com.dcsys.data.document;

import java.util.List;

import ar.com.dcsys.exceptions.DocumentException;

public interface DocumentDAO {

	public void persist(Document d) throws DocumentException;
	public List<String> findAll() throws DocumentException;
	public Document findById(String id) throws DocumentException;
	
}
