package ar.com.dcsys.model.filters;

import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.exceptions.FilterException;

public interface ReserveAttemptDateFilter {

	/**
	 * Chequea el reserveAttemptDate y retorna ok si cumple con las condiciones de este filtro.
	 * o sea es ACEPTADO
	 * en caso de no ser aceptado retorna FALSE
	 * dispara FilterException en el caso de no poder filtrar el reserveAttemptDate pasado como parámetro.
	 * @param ra
	 * @return
	 */
	public boolean filter(ReserveAttemptDate ra) throws FilterException;
	
	public Class getType();
	
}
