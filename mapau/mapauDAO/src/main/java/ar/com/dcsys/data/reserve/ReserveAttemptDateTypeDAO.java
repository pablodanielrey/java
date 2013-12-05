package ar.com.dcsys.data.reserve;

import java.io.Serializable;
import java.util.List;

import ar.com.dcsys.exceptions.MapauException;

public interface ReserveAttemptDateTypeDAO extends Serializable {

	public String persist(ReserveAttemptDateType object) throws MapauException;
	public void remove(ReserveAttemptDateType object) throws MapauException;
	
	public ReserveAttemptDateType findById(String id) throws MapauException;
	public List<ReserveAttemptDateType> findAll() throws MapauException;
	public ReserveAttemptDateType findByName(String name) throws MapauException;
	
	public List<String> findAllIds() throws MapauException;
}
