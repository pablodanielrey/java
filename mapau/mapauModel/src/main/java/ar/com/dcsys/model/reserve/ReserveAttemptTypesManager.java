package ar.com.dcsys.model.reserve;

import java.util.List;

import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.exceptions.MapauException;

public interface ReserveAttemptTypesManager {
	public String persist(ReserveAttemptDateType reserveAttemptType) throws MapauException;
	public void remove(ReserveAttemptDateType reserveAttemptType) throws MapauException;
	
	public ReserveAttemptDateType findById(String id) throws MapauException;
	public List<ReserveAttemptDateType> findAll() throws MapauException;
}
