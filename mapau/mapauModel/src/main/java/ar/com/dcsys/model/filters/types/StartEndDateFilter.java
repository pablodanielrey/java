package ar.com.dcsys.model.filters.types;

import java.util.Date;

import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.exceptions.FilterException;
import ar.com.dcsys.model.filters.ReserveAttemptDateFilter;

/**
 * Define fechas de inicio y fin de la consulta de los appointmnets.
 * Se implementa como filtro para unificar el pasaje de parámetros de consultas de appointments desde la interfazz grafica al modelo.
 * porque estos datos se van directo a los backedns y es preferible no procesarlos en el arbol de filtros
 * se implementan los getters para obtener esos datos.
 * en el caso de tener todos los appointments en una cache se puede implementar como filtro dentro dle arbol de filtros.
 * 
 * @author pablo
 *
 */

public final class StartEndDateFilter implements ReserveAttemptDateFilter {

	private final Date start;
	private final Date end;
	
	public StartEndDateFilter(Date start, Date end) {
		this.start = start;
		this.end = end;
	}
	
	public Date getStart() {
		return start;
	}
	
	public Date getEnd() {
		return end;
	}
	
	
	/**
	 * No se va a usar en esta forma, se van a obtener las fechas directo desde afuera
	 * pero por las dudas lo implemento ya que puede servir.
	 */
	@Override
	public boolean filter(ReserveAttemptDate ra) throws FilterException {
		if (ra.getStart() == null) {
			throw new FilterException("ReserveAttemptDate.start == null");
		}
		if (ra.getEnd() == null) {
			throw new FilterException("ReserveAttemptdate.end == null");
		}
		return start.before(ra.getStart()) && (ra.getEnd().before(end));
	}

	@Override
	public Class getType() {
		return StartEndDateFilter.class;
	}

}
