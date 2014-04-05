package ar.com.dcsys.model;

import java.util.List;

import javax.inject.Inject;

import ar.com.dcsys.data.document.Document;
import ar.com.dcsys.data.document.DocumentDAO;
import ar.com.dcsys.exceptions.DocumentException;

public class DocumentsManagerBean implements DocumentsManager {

	private final DocumentDAO documentDAO;
	
	@Inject
	public DocumentsManagerBean(DocumentDAO documentDAO) {
		this.documentDAO = documentDAO;
	}
	
	@Override
	public void persist(Document d) throws DocumentException {
		documentDAO.persist(d);
	}

	@Override
	public List<String> findAll() throws DocumentException {
		return documentDAO.findAll();
	}

	@Override
	public Document findById(String id) throws DocumentException {
		if (id == null) {
			throw new DocumentException("id == null");
		}
		return documentDAO.findById(id);
	}
	
	@Override
	public Document findByIdWithoutContent(String id) throws DocumentException {
		if (id == null) {
			throw new DocumentException("id == null");
		}
		return documentDAO.findByIdWithoutContent(id);
	}
	
}
