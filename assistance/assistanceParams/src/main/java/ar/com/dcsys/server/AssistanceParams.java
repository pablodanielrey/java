package ar.com.dcsys.server;

import java.util.Date;
import java.util.List;

import ar.com.dcsys.data.period.PeriodAssignation;
import ar.com.dcsys.data.period.PeriodDAO;
import ar.com.dcsys.data.period.PeriodType;
import ar.com.dcsys.data.period.Person;
import ar.com.dcsys.exceptions.PeriodException;

public class AssistanceParams implements PeriodDAO {

	@Override
	public List<PeriodAssignation> findAllPeriodAssignations()
			throws PeriodException, PersonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PeriodAssignation> findAll(Person p) throws PeriodException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String persist(PeriodAssignation p) throws PeriodException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(PeriodAssignation p) throws PeriodException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PeriodAssignation findBy(Person person, Date date, PeriodType type)
			throws PeriodException {
		// TODO Auto-generated method stub
		return null;
	}

}
