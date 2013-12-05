package ar.com.dcsys.model.reserve;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.data.reserve.ReserveAttemptDateTypeDAO;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.MapauNotFoundException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Singleton
public class ReserveAttemptTypesManagerBean implements ReserveAttemptTypesManager {

	private static Logger logger = Logger.getLogger(ReserveAttemptTypesManagerBean.class.getName());	
	
	private LoadingCache<String,ReserveAttemptDateType> ratCache;
	
	private final ReserveAttemptDateTypeDAO reserveAttemptTypeDAO;
	

	@Inject
	public ReserveAttemptTypesManagerBean(ReserveAttemptDateTypeDAO reserveAttemptTypeDAO) {
		this.reserveAttemptTypeDAO = reserveAttemptTypeDAO;
		createCaches();
	}
	
	private void createCaches() {
		/*
		 * creo la cache
		 */
		ratCache = CacheBuilder.newBuilder().maximumSize(100).build(new CacheLoader<String,ReserveAttemptDateType>() {

			@Override
			public ReserveAttemptDateType load(String key) throws Exception {
				ReserveAttemptDateType rat = reserveAttemptTypeDAO.findById(key);
				if (rat == null) {
					throw new MapauNotFoundException();
				}
				return rat;
			}
			
		});		
	}
	/*
	@PreDestroy
	private void destroy() {
		ratCache.cleanUp();
		ratCache.invalidateAll();
		ratCache = null;
		
		reserveAttemptTypeManager = null;
	}*/
	
	/**
	 * Invalida las caches correctas en caso de cambio de datos
	 * @param rat
	 */
	private void invalidateCaches(ReserveAttemptDateType rat) {
		String id = rat.getId();
		if (id != null) {
			ratCache.invalidate(id);
		}		
	}	
	
	
	@Override
	public List<ReserveAttemptDateType> findAll() throws MapauException {
		List<String> allIds = reserveAttemptTypeDAO.findAllIds();
		if (allIds == null || allIds.size() <= 0) {
			return new ArrayList<ReserveAttemptDateType>();
		}
		
		try {
			return ratCache.getAll(allIds).values().asList();
		} catch (ExecutionException e) {
			throw new MapauException(e.getCause().getMessage());
		}	
	}
	
	@Override
	public ReserveAttemptDateType findById(String id) throws MapauException {
		try {
			return ratCache.get(id);
		} catch (ExecutionException e) {
			if (e.getCause() instanceof MapauNotFoundException) {
				return null;
			} else {
				logger.log(Level.SEVERE, e.getCause().getMessage(),e.getCause());
				throw new MapauException(e.getCause().getMessage());
			}
		}	
	}
	
	@Override
	public String persist(ReserveAttemptDateType reserveAttemptType) throws MapauException {
		invalidateCaches(reserveAttemptType);
		
		String newId = reserveAttemptTypeDAO.persist(reserveAttemptType);
		
		return newId;	
	}
	
	@Override
	public void remove(ReserveAttemptDateType reserveAttemptType) throws MapauException {
		invalidateCaches(reserveAttemptType);
		reserveAttemptTypeDAO.remove(reserveAttemptType);
	}
}
