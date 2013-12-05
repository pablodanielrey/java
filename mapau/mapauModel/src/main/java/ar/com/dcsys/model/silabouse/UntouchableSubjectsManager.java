package ar.com.dcsys.model.silabouse;

import java.util.List;

import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.UntouchableSubject;
import ar.com.dcsys.exceptions.MapauException;

public interface UntouchableSubjectsManager {
	public String persist(UntouchableSubject us) throws MapauException;
	public void remove(UntouchableSubject us) throws MapauException;
	
	public UntouchableSubject findById(String id) throws MapauException;
	public List<UntouchableSubject> findAll() throws MapauException;
	public UntouchableSubject findBy(Area area) throws MapauException;
}
