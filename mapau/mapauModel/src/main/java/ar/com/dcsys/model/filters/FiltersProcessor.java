package ar.com.dcsys.model.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.exceptions.FilterException;

public class FiltersProcessor {

	
	private interface FilterProvider {
		public ReserveAttemptDateFilter newBinaryFilter(ReserveAttemptDateFilter f1, ReserveAttemptDateFilter f2);
	}

	private static final ReserveAttemptDateFilter treeFilterAssembler(FilterProvider fp, Queue<ReserveAttemptDateFilter> values) {
		
		ReserveAttemptDateFilter root = null;

		while (!values.isEmpty()) {
			
			if (values.size() == 1) {

				ReserveAttemptDateFilter f1 = values.remove();
				
				if (root == null) {
					root = f1;
				} else {
					root = fp.newBinaryFilter(root,f1);
				}
				
			} else if (values.size() >= 2) {
				
				ReserveAttemptDateFilter f1 = values.remove();
				ReserveAttemptDateFilter f2 = values.remove();
				
				if (root == null) {
					root = fp.newBinaryFilter(f1,f2);
				} else {
					root = fp.newBinaryFilter(root, fp.newBinaryFilter(f1,f2));
				}
				
			}
		}

		return root;
	}
	
	
	private static final FilterProvider andFilterProvider = new FilterProvider() {
		@Override
		public ReserveAttemptDateFilter newBinaryFilter(ReserveAttemptDateFilter f1, ReserveAttemptDateFilter f2) {
			return new AndFilter(f1,f2);
		}
	};
	
	private static final FilterProvider orFilterProvider = new FilterProvider() {
		@Override
		public ReserveAttemptDateFilter newBinaryFilter(ReserveAttemptDateFilter f1, ReserveAttemptDateFilter f2) {
			return new OrFilter(f1,f2);
		}
	};
	
	
	/**
	 * Arma el arbol de filtros dado una lista de filtros.
	 * los filtros que reprecentan las mismas clases se organizan en ORS (ej Curso = c1 o Curso = c2)
	 * los filtros que reprecentan diferentes clases se organizan en ANDS (ej Curso = c1 y Aulas = a1)
	 * 
	 * @param filters
	 * @return
	 * @throws FilterException
	 */
	public static ReserveAttemptDateFilter assembleTree(List<ReserveAttemptDateFilter> filters) throws FilterException {
		
		if (filters == null || filters.size() <= 0) {
			throw new FilterException();
		}
		
		Queue<ReserveAttemptDateFilter> q = new LinkedList<ReserveAttemptDateFilter>();
		q.addAll(filters);

		// clasifico todos los filtros en sus respectivas clases de filtros.
		Map<Class,Queue<ReserveAttemptDateFilter>> classify = new HashMap<>();
		while (!q.isEmpty()) {
			ReserveAttemptDateFilter f = q.remove();
			Class type = f.getType();
			
			Queue<ReserveAttemptDateFilter> fs = classify.get(type);
			if (fs == null) {
				fs = new LinkedList<>();
				classify.put(type,fs);
			}
			
			fs.add(f);
		}
		
		Queue<ReserveAttemptDateFilter> ors = new LinkedList<>();
		for (Class type : classify.keySet()) {
			Queue<ReserveAttemptDateFilter> values = classify.get(type);
			ReserveAttemptDateFilter tree = treeFilterAssembler(orFilterProvider, values);
			ors.add(tree);
		}

		ReserveAttemptDateFilter finalTree = treeFilterAssembler(andFilterProvider, ors);
		
		return finalTree;
	}

	
	/**
	 * Filtra los reserveAttemptDates mediante los filtros pasados como parámetro.
	 * se arma el arbol de filtros teniendo en cuenta ands y ors y se filtran todos los reservesattemptsdate 
	 * con el arbol. los que pasan todas las condiciones son retornados en el resultado.
	 * si ninguno pasa las condiciones entonces se retorna una lista bacía.
	 * @param filters
	 * @param ras
	 * @return
	 * @throws FilterException
	 */
	public static List<ReserveAttemptDate> filter(List<ReserveAttemptDateFilter> filters, List<ReserveAttemptDate> ras) throws FilterException {
		ReserveAttemptDateFilter root = assembleTree(filters);
		
		List<ReserveAttemptDate> filtered = new ArrayList<>();
		for (ReserveAttemptDate ra : ras) {

			if (root.filter(ra)) {
				filtered.add(ra);
			}
		}
		
		return filtered;
	}
	
}
